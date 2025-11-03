package got.client.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MovementInput;

@SideOnly(Side.CLIENT)
public class GOTMovementInputInverted extends MovementInput {

    public final MovementInput originalInput;

    public GOTMovementInputInverted(MovementInput original) {
        this.originalInput = original;
    }


    @Override
    public void updatePlayerMoveState() {
        this.originalInput.updatePlayerMoveState();

        this.moveForward = -this.originalInput.moveForward;

        this.moveStrafe = -this.originalInput.moveStrafe;

        this.jump = this.originalInput.sneak;
        this.sneak = this.originalInput.jump;
    }
}