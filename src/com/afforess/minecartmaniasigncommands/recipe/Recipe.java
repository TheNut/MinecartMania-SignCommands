package com.afforess.minecartmaniasigncommands.recipe;

import com.afforess.minecartmaniacore.Item;

public interface Recipe {
	
	public Item getResult();
	
	public int getNumRecipes();
	
	public Item[] getIngredients(int i);
}
