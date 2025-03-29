package baguchi.mob_enchant_with_tome.goal;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.registry.ModSounds;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import twilightforest.entity.monster.DeathTome;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DeathTomeEnchantGoal extends Goal {
    private final Mob mob;
    @Nullable
    private LivingEntity target;
    private int cooldown = 0;
    private int enchantTime = 0;

    private final Predicate<LivingEntity> fillter = (entity) -> {
        return !(entity instanceof DeathTome) && entity instanceof IEnchantCap enchantCap && !enchantCap.getEnchantCap().hasEnchant();
    };

    private final Predicate<LivingEntity> enchanted_fillter = (entity) -> {
        return !(entity instanceof DeathTome) && entity instanceof IEnchantCap enchantCap && enchantCap.getEnchantCap().hasEnchant();
    };

    public DeathTomeEnchantGoal(Mob rangedAttackMob) {

        this.mob = (Mob) rangedAttackMob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (this.cooldown <= 0) {
            if (livingentity != null && livingentity.isAlive()) {
                List<LivingEntity> list = this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().expandTowards(12.0D, 8.0D, 12.0D), this.fillter);
                if (list.isEmpty()) {
                    return false;
                } else {
                    List<LivingEntity> enchanted_list = this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().expandTowards(12.0D, 8.0D, 12.0D), this.enchanted_fillter);

                    //set enchant limit
                    if (enchanted_list.size() < 4) {
                        LivingEntity target = list.get(this.mob.getRandom().nextInt(list.size()));
                        if (target != this.mob.getTarget() && target != this.mob && target instanceof Monster) {
                            this.target = target;
                            this.cooldown = 300 + this.mob.getRandom().nextInt(300);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        } else {
            this.cooldown--;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.enchantTime < 60;
    }

    @Override
    public void start() {
        super.start();
        this.mob.playSound(SoundEvents.BOOK_PAGE_TURN);
    }

    @Override
    public void stop() {
        if (this.target instanceof IEnchantCap cap) {
            MobEnchantUtils.addUnstableRandomEnchantmentToEntity(this.target, this.mob, cap, this.mob.getRandom(), 10, ModTags.MobEnchantTags.ENCHANTER_ENCHANT);
        }
        this.mob.playSound(ModSounds.ENCHANTER_SPELL.get());
        this.target = null;

        this.enchantTime = 0;
        this.cooldown = 300 + this.mob.getRandom().nextInt(300);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.enchantTime++;
        if (this.target instanceof IEnchantCap cap) {
            this.mob.lookAt(this.target, 30, 30);
        }
    }


}