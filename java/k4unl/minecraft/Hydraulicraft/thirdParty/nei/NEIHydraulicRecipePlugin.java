package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

public abstract class NEIHydraulicRecipePlugin {//extends TemplateRecipeHandler {
    /*
    @Override
    public String getGuiTexture() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return null;
    }

    protected abstract CachedRecipe processRecipe(IFluidRecipe recipe);

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        CachedRecipe cachedRecipe = arecipes.get(recipe);

        if (cachedRecipe instanceof NEIHydraulicRecipe) { // better safe than sorry
            NEIHydraulicRecipe hydraulicRecipe = (NEIHydraulicRecipe) cachedRecipe;
            for (WidgetBase widget : hydraulicRecipe.getWidgets()) {
                widget.handletooltip(gui, currenttip, recipe);
            }
        }
        return super.handleTooltip(gui, currenttip, recipe);
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe cachedRecipe = arecipes.get(recipe);
        if (cachedRecipe instanceof NEIHydraulicRecipe) {
            NEIHydraulicRecipe hydraulicRecipe = (NEIHydraulicRecipe) cachedRecipe;
            for (WidgetBase widget : hydraulicRecipe.getWidgets()) {
                widget.render();
            }
        }
        super.drawExtras(recipe);
    }

    public abstract CachedRecipe getShape(IFluidRecipe recipe);

    public abstract List<IFluidRecipe> getRecipeCollection();

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            for (Object item : recipe.getInputItems()) {
                if (item instanceof ItemStack) {
                    if (ingredient.isItemEqual((ItemStack) item)) {
                        this.arecipes.add(getShape(recipe));
                        break;
                    }
                } else if (item instanceof String) {
                    int[] oreIds = OreDictionary.getOreIDs(ingredient);
                    for (int id : oreIds) {
                        String oreName = OreDictionary.getOreName(id);
                        if (oreName.equals(item)) {
                            this.arecipes.add(getShape(recipe));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        boolean retval = ((NEIHydraulicRecipe) this.arecipes.get(recipe)).mouseClicked(gui, button, recipe);
        if (retval)
            return retval;

        return super.mouseClicked(gui, button, recipe);
    }

    public void loadCraftingRecipes(FluidStack stack) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            if (recipe.getOutputFluids() != null)
                for (FluidStack fluidStack : recipe.getOutputFluids()) {
                    if (fluidStack.equals(stack))
                        this.arecipes.add(processRecipe(recipe));
                }
        }
    }

    public void loadUsageRecipes(FluidStack stack) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            if (recipe.getInputFluids() != null)
                for (FluidStack fluidStack : recipe.getInputFluids()) {
                    if (fluidStack.equals(stack))
                        this.arecipes.add(processRecipe(recipe));
                }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("fluid") && results.length >= 1 && results[0] instanceof FluidStack) {
            loadCraftingRecipes((FluidStack) results[0]);
        } else if (outputId.equals("item") && results.length >= 1 && results[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) results[0];
            if (itemStack.getItem() instanceof ItemBlock) {
                FluidStack stack = tryLoadingItemBlock((ItemBlock) (itemStack.getItem()));
                if (stack != null)
                    loadCraftingRecipes(stack);
            } else if (itemStack.getItem() instanceof ItemBucketBase) {
                ItemBucketBase bucketBase = (ItemBucketBase) itemStack.getItem();
                FluidStack stack = tryLoadingBlock(bucketBase.getFluidBlock());
                if (stack != null)
                    loadCraftingRecipes(stack);
            }
        }
        super.loadCraftingRecipes(outputId, results);
    }

    private FluidStack tryLoadingItemBlock(ItemBlock itemBlock) {
        return tryLoadingBlock(itemBlock.field_150939_a);
    }

    private FluidStack tryLoadingBlock(Block block) {
        if (block instanceof BlockBaseFluid) {
            BlockBaseFluid bbf = (BlockBaseFluid) block;
            return new FluidStack(bbf.getFluid(), 0);
        }

        return null;
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("fluid") && ingredients.length >= 1 && ingredients[0] instanceof FluidStack) {
            loadUsageRecipes((FluidStack) ingredients[0]);
        } else if (inputId.equals("item") && ingredients.length >= 1 && ingredients[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) ingredients[0];
            if (itemStack.getItem() instanceof ItemBlock) {
                FluidStack stack = tryLoadingItemBlock((ItemBlock) (itemStack.getItem()));
                if (stack != null)
                    loadUsageRecipes(stack);
            } else if (itemStack.getItem() instanceof ItemBucketBase) {
                ItemBucketBase bucketBase = (ItemBucketBase) itemStack.getItem();
                FluidStack stack = tryLoadingBlock(bucketBase.getFluidBlock());
                if (stack != null)
                    loadUsageRecipes(stack);
            }

        }
        super.loadUsageRecipes(inputId, ingredients);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            if (areItemStacksEqual(recipe.getRecipeOutput(), result, true)) { // TODO check for ChancedStack
                this.arecipes.add(getShape(recipe));
            }
        }
    }

    public boolean areItemStacksEqual(ItemStack a, ItemStack b, boolean matchNBT) {
        boolean tmp = NEIClientUtils.areStacksSameTypeCrafting(a, b);
        if (!matchNBT || !tmp)
            return tmp;

        if (a.getTagCompound() == null)
            return b.getTagCompound() == null;

        return a.getTagCompound().equals(b.getTagCompound());
    }

    public class NEIHydraulicRecipe extends CachedRecipe {
        private List<PositionedStack> input   = new ArrayList<PositionedStack>();
        private List<PositionedStack> output  = new ArrayList<PositionedStack>();
        private List<WidgetBase>      widgets = new ArrayList<WidgetBase>();

        public NEIHydraulicRecipe addInput(FluidStack fluidStack, int x, int y, int width, int height) {

            NEIWidgetTank tank = new NEIWidgetTank(fluidStack, x, y, width, height);
            widgets.add(tank);
            return this;
        }

        public NEIHydraulicRecipe addInput(PositionedStack[] itemStacks) {
            Collections.addAll(input, itemStacks);

            return this;
        }

        public NEIHydraulicRecipe addInput(PositionedStack stack) {
            input.add(stack);

            return this;
        }

        public NEIHydraulicRecipe addOutput(PositionedStack stack) {
            output.add(stack);

            return this;
        }

        public NEIHydraulicRecipe addOutput(FluidStack fluidStack, int x, int y) {
            // TODO fluid adding

            return this;
        }

        public NEIHydraulicRecipe addOutput(PositionedStack[] stacks) {
            Collections.addAll(output, stacks);

            return this;
        }

        public NEIHydraulicRecipe addWidget(WidgetBase widgetBase) {
            this.widgets.add(widgetBase);

            return this;
        }


        @Override
        public PositionedStack getResult() {
            if (output.size() == 1)
                return output.get(0);

            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return input;
        }

        public List<WidgetBase> getWidgets() {
            return widgets;
        }

        public boolean mouseClicked(GuiRecipe guiRecipe, int button, int recipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = guiRecipe.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - ((guiRecipe.width - 176) / 2) - offset.x, mousepos.y - ((guiRecipe.height - 166) / 2) - offset.y);

            for (WidgetBase widgetBase : widgets) {
                if (widgetBase.getBounds().contains(relMouse)) {
                    boolean retval = widgetBase.clicked(button);
                    if (retval)
                        return retval;
                }
            }
            return false;
        }
    }
    */
}
