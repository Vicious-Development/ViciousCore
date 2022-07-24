package com.vicious.viciouscore.common.recipe.ingredients.stack;

public abstract class IngredientStack<T> {
    public static final IngredientStack<?> EMPTY = new IngredientStack<>(null,0) {
        @Override
        public boolean matches(Object in) {
            return false;
        }

        @Override
        public Object deingredify() {
            return null;
        }
    };
    protected T representative;
    protected int count = 1;
    public IngredientStack(T representative){
        this.representative=representative;
    }
    public IngredientStack(T representative, int count){
        this.representative=representative;
        this.count=count;
    }
    public abstract boolean matches(Object in);

    public abstract T deingredify();

    public void increment(int incrementation) {
        count+=incrementation;
    }

    public int getCount() {
        return count;
    }

    public void decrement(int decrementation) {
        count-=decrementation;
    }
}
