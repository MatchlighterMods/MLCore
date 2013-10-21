package ml.core.gui.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ml.core.gui.GuiRenderUtils;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Will be paired w/ {@link TopParentGuiElement}.
 * Stores and handles the various click actions
 * @author Matchlighter
 */
@Deprecated
public class SlotManager {

	TopParentGuiElement owner;
	Minecraft mc;

	public SlotManager(TopParentGuiElement p) {
		owner = p;
		mc = owner.getGui().getMinecraft();
	}

	/** Used when touchscreen is enabled */
	private ItemStack draggedStack;
	private ItemStack returningStack;
	private boolean isRightMouseClick;
	private Slot clickedSlot;
	private Vector2i draggedStackPos;
	private Slot returningStackDestSlot;
	private long returningStackTime;

	private Slot field_92033_y;
	private long field_92032_z;
	protected final Set field_94077_p = new HashSet();
	protected boolean field_94076_q;
	private int field_94071_C;
	private int field_94067_D;
	private boolean field_94068_E;
	private int field_94069_F;
	private long field_94070_G;
	private Slot field_94072_H;
	private int field_94073_I;
	private boolean field_94074_J;
	private ItemStack field_94075_K;

	public void slotClicked(Slot slot, int mbutton) {
		boolean flag = mbutton == mc.gameSettings.keyBindPickBlock.keyCode + 100;
		long l = Minecraft.getSystemTime();
		this.field_94074_J = this.field_94072_H == slot && l - this.field_94070_G < 250L && this.field_94073_I == mbutton;
		this.field_94068_E = false;

		if (mbutton == 0 || mbutton == 1 || flag) {
			int k1 = -1;

			if (slot != null) {
				k1 = slot.slotNumber;
			}

//			if (mc.gameSettings.touchscreen && flag1 && mc.thePlayer.inventory.getItemStack() == null) {
//				mc.displayGuiScreen((GuiScreen)null);
//				return;
//			}

			if (k1 != -1) {
				if (mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						this.clickedSlot = slot;
						this.draggedStack = null;
						this.isRightMouseClick = mbutton == 1;
					} else {
						this.clickedSlot = null;
					}
				} else if (!this.field_94076_q) {
					if (mc.thePlayer.inventory.getItemStack() == null) {
						if (mbutton == mc.gameSettings.keyBindPickBlock.keyCode + 100) {
							this.handleAction(slot, k1, mbutton, 3);
						} else {
							boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							byte b0 = 0;

							if (flag2) {
								this.field_94075_K = slot != null && slot.getHasStack() ? slot.getStack() : null;
								b0 = 1;
							} else if (k1 == -999) {
								b0 = 4;
							}

							this.handleAction(slot, k1, mbutton, b0);
						}

						this.field_94068_E = true;
					} else {
						this.field_94076_q = true;
						this.field_94067_D = mbutton;
						this.field_94077_p.clear();

						if (mbutton == 0) {
							this.field_94071_C = 0;
						} else if (mbutton == 1) {
							this.field_94071_C = 1;
						}
					}
				}
			}
		}

		this.field_94072_H = slot;
		this.field_94070_G = l;
		this.field_94073_I = mbutton;
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
	 * lastButtonClicked & timeSinceMouseClick.
	 */
	public void mouseClickMove(Slot slot, int button, long timeSinceLast) {
		ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

		if (this.clickedSlot != null && mc.gameSettings.touchscreen) {
			if (button == 0 || button == 1) {
				if (this.draggedStack == null) {
					if (slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack().copy();
					}
				} else if (this.draggedStack.stackSize > 1 && slot != null && Container.func_94527_a(slot, this.draggedStack, false)) {
					long i1 = Minecraft.getSystemTime();

					if (this.field_92033_y == slot) {
						if (i1 - this.field_92032_z > 500L) {
							this.handleAction(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.handleAction(slot, slot.slotNumber, 1, 0);
							this.handleAction(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.field_92032_z = i1 + 750L;
							--this.draggedStack.stackSize;
						}
					} else {
						this.field_92033_y = slot;
						this.field_92032_z = i1;
					}
				}
			}
		} else if (this.field_94076_q && slot != null && itemstack != null && itemstack.stackSize > this.field_94077_p.size() && Container.func_94527_a(slot, itemstack, true) && slot.isItemValid(itemstack) && owner.getContainer().canDragIntoSlot(slot)) {
			this.field_94077_p.add(slot);
			this.func_94066_g();
		}
	}

	private void func_94066_g() {
		ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && this.field_94076_q) {
			this.field_94069_F = itemstack.stackSize;
			ItemStack itemstack1;
			int i;

			for (Iterator iterator = this.field_94077_p.iterator(); iterator.hasNext(); this.field_94069_F -= itemstack1.stackSize - i) {
				Slot slot = (Slot)iterator.next();
				itemstack1 = itemstack.copy();
				i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.func_94525_a(this.field_94077_p, this.field_94071_C, itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getSlotStackLimit()) {
					itemstack1.stackSize = slot.getSlotStackLimit();
				}
			}
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	public void mouseMovedOrUp(Slot slot, int which) {
		int j1 = -1;

		if (slot != null) {
			j1 = slot.slotNumber;
		}

		Slot slot1;
		Iterator iterator;

		if (this.field_94074_J && slot != null && which == 0 && owner.getContainer().func_94530_a((ItemStack)null, slot)) {
			if (owner.getGui().isShiftKeyDown()) {
				if (slot != null && slot.inventory != null && this.field_94075_K != null) {
					iterator = owner.getContainer().inventorySlots.iterator();

					while (iterator.hasNext()) {
						slot1 = (Slot)iterator.next();

						if (slot1 != null && slot1.canTakeStack(mc.thePlayer) && slot1.getHasStack() && slot1.inventory == slot.inventory && Container.func_94527_a(slot1, this.field_94075_K, true)) {
							this.handleAction(slot1, slot1.slotNumber, which, 1);
						}
					}
				}
			} else {
				this.handleAction(slot, j1, which, 6);
			}

			this.field_94074_J = false;
			this.field_94070_G = 0L;
		} else {
			if (this.field_94076_q && this.field_94067_D != which) {
				this.field_94076_q = false;
				this.field_94077_p.clear();
				this.field_94068_E = true;
				return;
			}

			if (this.field_94068_E) {
				this.field_94068_E = false;
				return;
			}

			boolean flag1;

			if (this.clickedSlot != null && mc.gameSettings.touchscreen) {
				if (which == 0 || which == 1) {
					if (this.draggedStack == null && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack();
					}

					flag1 = Container.func_94527_a(slot, this.draggedStack, false);

					if (j1 != -1 && this.draggedStack != null && flag1) {
						this.handleAction(this.clickedSlot, this.clickedSlot.slotNumber, which, 0);
						this.handleAction(slot, j1, 0, 0);

						if (mc.thePlayer.inventory.getItemStack() != null) {
							this.handleAction(this.clickedSlot, this.clickedSlot.slotNumber, which, 0);
							this.draggedStackPos = owner.getLocalMousePos();
							this.returningStackDestSlot = this.clickedSlot;
							this.returningStack = this.draggedStack;
							this.returningStackTime = Minecraft.getSystemTime();
						} else {
							this.returningStack = null;
						}
					} else if (this.draggedStack != null) {
						this.draggedStackPos = owner.getLocalMousePos();
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					}

					this.draggedStack = null;
					this.clickedSlot = null;
				}
			} else if (this.field_94076_q && !this.field_94077_p.isEmpty()) {
				this.handleAction((Slot)null, -999, Container.func_94534_d(0, this.field_94071_C), 5);
				iterator = this.field_94077_p.iterator();

				while (iterator.hasNext()) {
					slot1 = (Slot)iterator.next();
					this.handleAction(slot1, slot1.slotNumber, Container.func_94534_d(1, this.field_94071_C), 5);
				}

				this.handleAction((Slot)null, -999, Container.func_94534_d(2, this.field_94071_C), 5);
			} else if (mc.thePlayer.inventory.getItemStack() != null) {
				if (which == mc.gameSettings.keyBindPickBlock.keyCode + 100) {
					this.handleAction(slot, j1, which, 3);
				} else {
					flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

					if (flag1) {
						this.field_94075_K = slot != null && slot.getHasStack() ? slot.getStack() : null;
					}

					this.handleAction(slot, j1, which, flag1 ? 1 : 0);
				}
			}
		}

		if (mc.thePlayer.inventory.getItemStack() == null) {
			this.field_94070_G = 0L;
		}

		this.field_94076_q = false;
	}

	protected void handleAction(Slot slot, int slnum, int button, int action) {
		owner.getGui().getMinecraft().playerController.windowClick(owner.getContainer().windowId, slnum, button, action, owner.getGui().getMinecraft().thePlayer);
	}
	
	@SideOnly(Side.CLIENT)
	protected static RenderItem itemRenderer = new RenderItem();
	
	@SideOnly(Side.CLIENT)
	public void drawSlotInventory(int x, int y, Slot par1Slot) {
		ItemStack itemstack = par1Slot.getStack();
		boolean flag = false;
		boolean flag1 = par1Slot == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
		ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
		String s = null;
		
		if (par1Slot == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		} else if (this.field_94076_q && this.field_94077_p.contains(par1Slot) && itemstack1 != null) {
			if (this.field_94077_p.size() == 1) {
				return;
			}

			if (Container.func_94527_a(par1Slot, itemstack1, true) && owner.getContainer().canDragIntoSlot(par1Slot)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.func_94525_a(this.field_94077_p, this.field_94071_C, itemstack, par1Slot.getStack() == null ? 0 : par1Slot.getStack().stackSize);

				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}

				if (itemstack.stackSize > par1Slot.getSlotStackLimit()) {
					s = EnumChatFormatting.YELLOW + "" + par1Slot.getSlotStackLimit();
					itemstack.stackSize = par1Slot.getSlotStackLimit();
				}
			} else {
				this.field_94077_p.remove(par1Slot);
				this.func_94066_g();
			}
		}

		itemRenderer.zLevel = 100.0F;

		if (itemstack == null) {
			Icon icon = par1Slot.getBackgroundIconIndex();

			if (icon != null) {
				GL11.glDisable(GL11.GL_LIGHTING);
				mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
				GuiRenderUtils.drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				GuiRenderUtils.drawGradientRect(x, y, x + 16, y + 16, -2130706433, -2130706433);
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, x, y);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, x, y, s);
		}

		itemRenderer.zLevel = 0.0F;
	}
}
