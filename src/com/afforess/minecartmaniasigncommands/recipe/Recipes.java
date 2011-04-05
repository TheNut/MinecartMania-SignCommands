package com.afforess.minecartmaniasigncommands.recipe;

import com.afforess.minecartmaniacore.Item;

public enum Recipes implements Recipe{
	
	Wood {
		public Item getResult() { return getItemWithAmount(Item.WOOD, 4); }
		public int getNumRecipes() { return 1; }
		public Item[] getIngredients(int i) { 
			Item[] list = { getItemWithAmount(Item.LOG, 1) };
			return list; 
		}
	},
	Stick {
		public Item getResult() { return getItemWithAmount(Item.STICK, 4); }
		public int getNumRecipes() { return 1; }
		public Item[] getIngredients(int i) { 
			Item[] list = { getItemWithAmount(Item.WOOD, 2) };
			return list; 
		}
	},
	Torch {
		public Item getResult() { return getItemWithAmount(Item.TORCH, 4); }
		public int getNumRecipes() { return 1; }
		public Item[] getIngredients(int i) { 
			Item[][] list = { { getItemWithAmount(Item.STICK, 1), getItemWithAmount(Item.COAL, 1) }, { getItemWithAmount(Item.STICK, 1), getItemWithAmount(Item.CHARCOAL, 1) } };
			return list[i];
		}
	};

	private static Item getItemWithAmount(Item item, int amt) {
		//item.setAmount(amt);
		return item;
	}

}

