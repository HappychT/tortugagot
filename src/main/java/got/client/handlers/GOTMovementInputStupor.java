package got.client.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MovementInput;

@SideOnly(Side.CLIENT)
public class GOTMovementInputStupor extends MovementInput {

    public final MovementInput originalInput;

    public GOTMovementInputStupor(MovementInput original) {
        this.originalInput = original;
    }

    @Override
    public void updatePlayerMoveState() {
        this.originalInput.updatePlayerMoveState();
        this.moveForward = 0;
        this.moveStrafe = 0;
        this.jump = false;
        this.sneak = false;
    }
}
