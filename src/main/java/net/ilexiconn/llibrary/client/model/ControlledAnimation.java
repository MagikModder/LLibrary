package net.ilexiconn.llibrary.client.model;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is a timer that can be used to easily animate models between poses. You
 * have to set the number of ticks between poses, increase or decrease the
 * timer, and get the percentage using a specific function.
 *
 * @author RafaMv & iLexiconn
 */
@SideOnly(Side.CLIENT)
public class ControlledAnimation
{
    /**
     * It is the timer used to animate
     */
    private double timer;

    /**
     * It is the limit time, the maximum value that the timer can be. I
     * represents the duration of the animation
     */
    private double duration;

    private double timerChange;

    public ControlledAnimation(int d)
    {
        timer = 0;
        duration = (double) d;
    }

    /**
     * Sets the duration of the animation in ticks. Try values around 50.
     *
     * @param d is the maximum number of ticks that the timer can reach.
     */
    public void setDuration(int d)
    {
        timer = 0;
        duration = (double) d;
    }

    /**
     * Returns the timer of this animation. Useful to save the progress of the animation.
     */
    public double getTimer()
    {
        return timer;
    }

    /**
     * Sets the timer to a specific value.
     *
     * @param t is the number of ticks to be set.
     */
    public void setTimer(int t)
    {
        timer = (double) t;

        if (timer > duration) timer = duration;
        else if (timer < 0) timer = 0;
    }

    /**
     * Sets the timer to 0.
     */
    public void resetTimer()
    {
        timer = 0;
    }

    /**
     * Increases the timer by 1.
     */
    public void increaseTimer()
    {
        if (timer < duration)
        {
            timer++;
            timerChange = 1;
        }
    }

    /**
     * Checks if the timer can be increased
     */
    public boolean canIncreaseTimer()
    {
        return timer < duration;
    }

    /**
     * Increases the timer by a specific value.
     *
     * @param t is the number of ticks to be increased in the timer
     */
    public void increaseTimer(int t)
    {
        if (timer + (double) t < duration) timer += (double) t;
        else timer = duration;
    }

    /**
     * Decreases the timer by 1.
     */
    public void decreaseTimer()
    {
        if (timer > 0d)
        {
            timer--;
            timerChange = -1;
        }
    }

    /**
     * Checks if the timer can be decreased
     */
    public boolean canDecreaseTimer()
    {
        return timer > 0d;
    }

    /**
     * Decreases the timer by a specific value.
     *
     * @param time is the number of ticks to be decreased in the timer
     */
    public void decreaseTimer(int time)
    {
        if (timer - (double) time > 0d) timer -= (double) time;
        else timer = 0d;
    }

    /**
     * Returns a float that represents a fraction of the animation, a value between 0f and 1f.
     */
    public float getAnimationFraction()
    {
        return (float) (timer / duration);
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using 1/(1 + e^(4-8*x)). It
     * is quite uniform but slow, and needs if statements.
     */
    public float getAnimationProgressSmooth()
    {
        if (timer > 0d)
        {
            if (timer < duration) return (float) (1d / (1d + Math.exp(4d - 8d * (timer / duration))));
            else return 1f;
        }
        return 0f;
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using 1/(1 + e^(6-12*x)). It
     * is quite uniform, but fast.
     */
    public float getAnimationProgressSteep()
    {
        return (float) (1d / (1d + Math.exp(6d - 12d * (timer / duration))));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using a sine function. It is
     * fast in the beginning and slow in the end.
     */
    public float getAnimationProgressSin()
    {
        return MathHelper.sin(1.57079632679f * (float) (timer / duration));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using a sine function
     * squared. It is very smooth.
     */
    public float getAnimationProgressSinSqrt()
    {
        float result = MathHelper.sin(1.57079632679f * (float) (timer / duration));
        return result * result;
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using a sine function to the
     * power of ten. It is slow in the beginning and fast in the end.
     */
    public float getAnimationProgressSinToTen()
    {
        return (float) Math.pow((double) MathHelper.sin(1.57079632679f * (float) (timer / duration)), 10);
    }

    @SideOnly(Side.CLIENT)
    public float getAnimationProgressSinToTenWithoutReturn()
    {
        if (timerChange == -1)
            return MathHelper.sin(1.57079632679f * (float) (timer / duration)) * MathHelper.sin(1.57079632679f * (float) (timer / duration));
        return (float) Math.pow((double) MathHelper.sin(1.57079632679f * (float) (timer / duration)), 10);
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using a sine function to a
     * specific power "i."
     *
     * @param i is the power of the sine function.
     */
    public float getAnimationProgressSinPowerOf(int i)
    {
        return (float) Math.pow((double) MathHelper.sin(1.57079632679f * (float) (timer / duration)), i);
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using x^2 / (x^2 + (1-x)^2).
     * It is smooth.
     */
    public float getAnimationProgressPoly2()
    {
        float x = (float) (timer / duration);
        float x2 = x * x;
        return x2 / (x2 + (1 - x) * (1 - x));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using x^3 / (x^3 + (1-x)^3).
     * It is steep.
     */
    public float getAnimationProgressPoly3()
    {
        float x = (float) (timer / duration);
        float x3 = x * x * x;
        return x3 / (x3 + (1 - x) * (1 - x) * (1 - x));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using x^n / (x^n + (1-x)^n).
     * It is steeper when n increases.
     *
     * @param n is the power of the polynomial function.
     */
    public float getAnimationProgressPolyN(int n)
    {
        double x = timer / duration;
        double xi = Math.pow(x, (double) n);
        return (float) (xi / (xi + Math.pow((1d - x), (double) n)));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. It reaches 1f using 0.5 + arctan(PI * (x -
     * 0.5)) / 2.00776964. It is super smooth.
     */
    public float getAnimationProgressArcTan()
    {
        return (float) (0.5f + 0.49806510671f * Math.atan(3.14159265359d * (timer / duration - 0.5d)));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration of the animation.
     * This value starts at 1f and ends at 1f.
     * The equation used is 0.5 - 0.5 * cos(2 * PI * x + sin(2 * PI * x)). It is smooth.
     */
    public float getAnimationProgressTemporary()
    {
        float x = 6.28318530718f * (float) (timer / duration);
        return 0.5f - 0.5f * MathHelper.cos(x + MathHelper.sin(x));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration
     * of the animation. This value starts at 0f and ends at 0f.
     * The equation used is sin(x * PI + sin(x * PI)). It is fast in the beginning and slow in the end.
     */
    public float getAnimationProgressTemporaryFS()
    {
        float x = 3.14159265359f * (float) (timer / duration);
        return MathHelper.sin(x + MathHelper.sin(x));
    }

    /**
     * Returns a value between 0f and 1f depending on the timer and duration of the animation.
     * This value starts at 1f and ends at 1f.
     * The equation used is 0.5 + 0.5 * cos(2 PI * x + sin(2 * PI * x)). It is smooth.
     */
    public float getAnimationProgressTemporaryInvesed()
    {
        float x = 6.28318530718f * (float) (timer / duration);
        return 0.5f + 0.5f * MathHelper.cos(x + MathHelper.sin(x));
    }
}
