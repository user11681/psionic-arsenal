/*Copyright 2019 Dudblockman

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.*/
package transfarmer.psionicarsenal.util;

import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import transfarmer.psionicarsenal.entity.EntityPsiArrow;
import transfarmer.psionicarsenal.entity.capability.ArrowSpellImmuneCapability;
import vazkii.psi.common.entity.EntitySpellProjectile;

@EventBusSubscriber(modid = "psionicarsenal")
public class EventHandler {
    @SubscribeEvent
    public static void attachArrowSpellImmunity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityArrow) {
            event.addCapability(new ResourceLocation("psionicarsenal", "psionicarsenalspellimmunearrow"), new ArrowSpellImmuneCapability(event.getObject()));
        }

    }

    @SubscribeEvent
    public static void arrowHit(final ProjectileImpactEvent event) {
        final Entity projectile = event.getEntity();

        if (projectile instanceof EntityArrow && !(projectile instanceof EntityPsiArrow) && NBTHelper.hasKey(projectile.getEntityData(), "rpsideas-spellimmune")) {
            for (final Entity rider : projectile.getPassengers()) {
                if (rider instanceof EntitySpellProjectile) {
                    rider.dismountRidingEntity();
                    rider.setPosition(projectile.posX, projectile.posY, projectile.posZ);
                    rider.motionX = projectile.motionX;
                    rider.motionY = projectile.motionY;
                    rider.motionZ = projectile.motionZ;
                    rider.velocityChanged = true;
                }
            }
        }
    }
}
