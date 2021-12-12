package com.vicious.viciouscore.common.configuration;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import com.vicious.viciouscore.common.util.tracking.configuration.Config;
import com.vicious.viciouscore.common.util.tracking.configuration.ConfigurationValue;
import com.vicious.viciouscore.common.util.tracking.serialization.SerializableArray;
import com.vicious.viciouscore.common.util.tracking.serialization.SerializationUtil;
import com.vicious.viciouscore.common.util.tracking.values.TrackableArrayValue;
import com.vicious.viciouscore.common.util.tracking.values.TrackableEnum;
import com.vicious.viciouscore.common.util.tracking.values.TrackableObject;
import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class StructureComponentConfiguration extends Config {
    public TrackableObject<String> name = add(new TrackableObject<>("name",()->"",this));

    public TrackableObject<Integer> dimX = add(new TrackableObject<>("dimX",()->0,this));
    public TrackableObject<Integer> dimY = add(new TrackableObject<>("dimY",()->0,this));
    public TrackableObject<Integer> dimZ = add(new TrackableObject<>("dimZ",()->0,this));

    public TrackableObject<Double> worldSpacing = add(new TrackableObject<>("minDistFromSameStructure",()->50.0,this));


    public TrackableObject<Boolean> canSpawnRandomly = add(new TrackableObject<>("canSpawnRandomly",()->false,this));
    public TrackableObject<Double> randomSpawnChance = add(new TrackableObject<>("randomSpawnChance",()->0.0001,this));

    public TrackableObject<Boolean> doHide = add(new TrackableObject<>("shouldBeHidden",()->false,this));

    public TrackableObject<Boolean> isBiomeBlacklist = add(new TrackableObject<>("isBiomeBlacklist",()->false,this));
    public TrackableArrayValue<String> biomeList = new TrackableArrayValue<>("biomeWhitelist",String.class,this,0);

    public TrackableObject<Boolean> isWorldBlacklist = add(new TrackableObject<>("isWorldBlacklist",()->false,this));
    public TrackableArrayValue<String> worldList = new TrackableArrayValue<>("worldWhitelist", String.class,this,0);

    public TrackableEnum<EnumFacing> frontDirection = add(new TrackableEnum<>("frontDirection", ()->EnumFacing.NORTH,this));
    public TrackableArrayValue<IBlockState> palette = add(new TrackableArrayValue<>("palette", IBlockState.class, this, 0));
    public TrackableArrayValue<Template.BlockInfo> blockInfo = add(new TrackableArrayValue<>("blocks", Template.BlockInfo.class, this, 0));

    public StructureComponentConfiguration(String name) {
        super(Directories.directorize(Directories.viciousStructuresDirectory.toAbsolutePath().toString(),name + ".json"));
        this.name.set(name);
    }

    @Override
    public void overWriteFile() {
        StringWriter writer = new StringWriter();
        writer.write("{");
        TrackableValue<?>[] vals = values.values().toArray(new TrackableValue<?>[0]);
        for (int i = 0; i < vals.length; i++) {
            try {
                TrackableValue<?> value = vals[i];
                writer.append("\n");
                if (value.value() == null) continue;
                if(value == blockInfo){
                    writer.append('"' + value.name + '"' + ": ");
                    writeValue(writer, value.value(), 0, 0, palette.toArrayList());
                    continue;
                }
                else if(value == palette){
                    writer.append('"' + value.name + '"' + ": ");
                    writeValue(writer, value.value(), 0, 0);
                    writer.append(",");
                    continue;
                }
                else if (value instanceof ConfigurationValue<?>) {
                    writer.append(((ConfigurationValue<?>) value).getTab() + '"' + value.name + '"' + ": ");
                    writeValue(writer, ((ConfigurationValue<?>) value).getStopValue(), 0, 0);
                } else {
                    writer.append('"' + value.name + '"' + ": ");
                    writeValue(writer, value.value(), 0, 0);
                }
                if (i < vals.length - 1) writer.append(",");
            } catch (Exception e) {
                ViciousCore.logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        writer.append("\n}");
        FileUtil.createOrWipe(PATH);
        try {
            Files.write(PATH, writer.toString().getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            ViciousCore.logger.error("Failed to save a Config " + getClass().getCanonicalName() + " caused by: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public StructureComponentConfiguration readFromJSON() {
        try {
            JSONObject obj = FileUtil.loadJSON(PATH);
            for (TrackableValue<?> value : values.values()) {
                if(value == blockInfo){
                    palette.setFromJSON(jsonObj);
                    ((TrackableArrayValue<Template.BlockInfo>) value).set(parseBlocks(obj));
                }
                else value.setFromJSON(obj);
            }
        } catch(Exception e){
            //IOE happens if the file doesn't exist. If it doesn't no values will be updated anyways which is totally fine.
            if(!(e instanceof FileNotFoundException)) {
                ViciousCore.logger.error("Failed to read a jsontrackable " + getClass().getCanonicalName() + " caused by: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return this;
    }

    private SerializableArray<Template.BlockInfo> parseBlocks(JSONObject obj) {
        return SerializationUtil.parseBlockInfoList(obj.getString("blocks"),palette);
    }

    public void writeBlocks(World in, BlockPos pos1, BlockPos pos2){
        List<Template.BlockInfo> list = Lists.newArrayList();
        List<Template.BlockInfo> list1 = Lists.newArrayList();
        this.dimX.set(Math.abs(pos1.getX()-pos2.getX()));
        this.dimY.set(Math.abs(pos1.getY()-pos2.getY()));
        this.dimZ.set(Math.abs(pos1.getZ()-pos2.getZ()));
        //BlockPos center = center(pos1,pos2);
        for (BlockPos.MutableBlockPos worldPos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
            BlockPos relativepos = worldPos.subtract(pos1);
            IBlockState iblockstate = in.getBlockState(worldPos);
            indexOfState(iblockstate);
            TileEntity tileentity = in.getTileEntity(worldPos);
            if (tileentity != null) {
                NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                nbttagcompound.removeTag("x");
                nbttagcompound.removeTag("y");
                nbttagcompound.removeTag("z");
                list1.add(new Template.BlockInfo(relativepos, iblockstate, nbttagcompound));
            } else {
                list.add(new Template.BlockInfo(relativepos, iblockstate, null));
            }
        }
        blockInfo.clear();
        blockInfo.addAll(list);
        blockInfo.addAll(list1);
    }

    /**
     * Replaces a block and its TE if necessary/
     */
    private void replaceBlockInWorld(World world, BlockPos pos, Template.BlockInfo info, PlacementSettings settings){
        IBlockState block = world.getBlockState(pos);
        Block replacement = info.blockState.getBlock();
        if(block.getBlock() != replacement){
            purgeTileEntities(world, pos, info);
        }
        if(block instanceof ITileEntityProvider) setTileEntityTags(world, pos, info, settings);
    }


    /**
     * Removes tile entities if they existed.
     */
    private void purgeTileEntities(World world, BlockPos pos, Template.BlockInfo info){
        world.setBlockState(pos,info.blockState);
        if (info.tileentityData != null)
        {
            TileEntity te = world.getTileEntity(pos);
            if (te != null)
            {
                if (te instanceof IInventory) ((IInventory)te).clear();
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            }
        }
    }

    /**
     * Updates the block's tileTags.
     */
    private void setTileEntityTags(World world, BlockPos pos, Template.BlockInfo info, PlacementSettings settings){
        TileEntity te = world.getTileEntity(pos);
        if(te == null) return;
        info.tileentityData.setInteger("x", pos.getX());
        info.tileentityData.setInteger("y", pos.getY());
        info.tileentityData.setInteger("z", pos.getZ());
        te.readFromNBT(info.tileentityData);
        te.mirror(settings.getMirror());
        te.rotate(settings.getRotation());
    }

    /**
     * Sends block updates to neighbors.
     */
    private void updateBlock(World world, BlockPos pos, Template.BlockInfo info){
        IBlockState block = world.getBlockState(pos);
        Block replacement = info.blockState.getBlock();
        if (block.getBlock() != replacement)
        {
            world.notifyNeighborsRespectDebug(pos, info.blockState.getBlock(), false);
            if (info.tileentityData != null)
            {
                TileEntity te = world.getTileEntity(pos);
                if (te != null)
                {
                    te.markDirty();
                }
            }
        }
    }
    public void replaceBlocksInWorld(World world, BlockPos originPos, PlacementSettings settings){
        this.blockInfo.forEach((blockInfo->{
            if(blockInfo != null) {
                BlockPos worldPos = originPos.add(blockInfo.pos);
                System.out.println(worldPos);
                replaceBlockInWorld(world, worldPos, blockInfo, settings);
            }
        }));
        this.blockInfo.forEach((blockInfo->{
            if(blockInfo != null) {
                BlockPos worldPos = originPos.add(blockInfo.pos);
                updateBlock(world, worldPos, blockInfo);
            }
        }));
    }
    public void replaceBlocksInWorld(World world, BlockPos position) {
        replaceBlocksInWorld(world,position,new PlacementSettings());
    }
    public boolean shouldSpawnRandomly(World world, BlockPos position){
        //Things that cannot spawn randomly usually won't call this method anyways so there's not really a need for this.
        //This is here so people don't register register world spawning structures without setting their spawn chance.
        if(!canSpawnRandomly.value()) return false;
        if(!isWorldValid(world)) return false;
        if(!isBiomeValid(world,position)) return false;
        if(!hidden(world,position)) return false;
        if(world.findNearestStructure(this.name.value(),position,false).distanceSq(position) < worldSpacing.value()) return false;
        return Math.random() > this.randomSpawnChance.value();
    }

    /**
     * Checks if the box is exposed on any walls, this isn't really perfect as it assumes the structure is in a rectangular prism area.
     */
    public boolean hidden(World world, BlockPos position){
        if(!doHide.value()) return true;
        //Left Z wall.
        for (int x = 1; x < dimX.value()-1; x++) {
            for(int y = 1; y < dimY.value()-1; y++) {
                BlockPos pos = position.add(x, y, -1);
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        //Right Z wall.
        for (int x = 1; x < dimX.value()-1; x++) {
            for(int y = 1; y < dimY.value()-1; y++) {
                BlockPos pos = position.add(x, y, 1+ dimZ.value());
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        //Left X wall.
        for (int z = 1; z < dimZ.value()-1; z++) {
            for(int y = 1; y < dimY.value()-1; y++) {
                BlockPos pos = position.add(-1, y, z);
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        //Right X wall.
        for (int z = 1; z < dimZ.value()-1; z++) {
            for(int y = 1; y < dimY.value()-1; y++) {
                BlockPos pos = position.add(1+dimX.value(), y, z);
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        //Bottom Y wall.
        for (int x = 1; x < dimX.value()-1; x++) {
            for(int z = 1; z < dimZ.value()-1; z++) {
                BlockPos pos = position.add(x, -1, z);
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        //Top Y wall.
        for (int x = 1; x < dimX.value()-1; x++) {
            for(int z = 1; z < dimZ.value()-1; z++) {
                BlockPos pos = position.add(x, 1+dimY.value(), z);
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
            }
        }
        return true;
    }
    public boolean isBiomeValid(World world, BlockPos position){
        Biome biome = world.getBiome(position);
        String regid = biome.delegate + ":" + biome.getRegistryName();
        if(biomeList.contains(regid)){
            return !isBiomeBlacklist.value();
        }
        else return isBiomeBlacklist.value();
    }
    public boolean isWorldValid(World world){
        String regid = world.provider.getDimensionType().getName();
        if(worldList.contains(regid)){
            return !isWorldBlacklist.value();
        }
        else return isWorldBlacklist.value();
    }


    private static BlockPos center(BlockPos pos1, BlockPos pos2) {
        int x = (pos1.getX() + pos2.getX())/2;
        int y = (pos1.getY() + pos2.getY())/2;
        int z = (pos1.getZ() + pos2.getZ())/2;
        return new BlockPos(x,y,z);
    }
    /*
        net.minecraftforge.fml.common.FMLCommonHandler.instance().getDataFixer().writeVersionData(nbt); //Moved up for MC updating reasons.
        nbt.setTag("palette", nbttaglist2);
        nbt.setTag("blocks", nbttaglist);
        nbt.setTag("entities", nbttaglist1);
        BlockPos size = (BlockPos) Reflection.accessField(this,"size");
        String author = (String) Reflection.accessField(this,"author");
        nbt.setTag("size", this.writeInts(size.getX(), size.getY(), size.getZ()));
        nbt.setString("author", author);
        nbt.setInteger("DataVersion", 1343);
        return nbt;
        */
    public int indexOfState(IBlockState state){
        int i = this.palette.indexOf(state);
        if(i == -1) return mapState(state);
        return i;
    }
    public int mapState(IBlockState state){
        palette.add(state);
        return palette.size()-1;
    }

    public BlockPos pos1() {
        return new BlockPos(0,0,0);
    }
    public BlockPos pos2() {
        return new BlockPos(0,0,0);
    }
}
