package bl4ckscor3.plugin.animalessentials.save;

public class SpawningPig extends Spawning
{
	private boolean saddle = false;
	
	public SpawningPig(EnumSpawningType t)
	{
		super(t);
	}
	
	public void setSaddle(boolean s)
	{
		saddle = s;
	}
	
	public boolean hasSaddle()
	{
		return saddle;
	}
}
