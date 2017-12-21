package faceduck.actors;

import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.World;
import faceduck.ai.AlmightyAI;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;
import faceduck.skeleton.util.Direction;

/**
 * 
 * The Almighty is an {@link Animal} that teleports anywhere randomly, and 
 * perform some amazing abilities that only Almighty can do.
 * 
 * Almighty's COOL_DOWN is 0, and Almighty has a viewRange of 10.
 * 
 * Almighty has maximum energy of 100, but he can heal himself. So Almighty
 * does not need to eat.
 * 
 * But, Almighty hates Gnat, so only thing he eats is Gnat.
 * 
 * Almighty's goal is to make a balanced world that nobody extincts.
 * 
 * @author Administrator
 *
 */
public class Almighty implements Animal {
	private static final int ALM_MAX_ENERGY = 100;
	private static final int AlM_VIEW_RANGE = 10;
	private static final int ALM_COOL_DOWN = 0;
	private static final int ALM_INITIAL_ENERGY = ALM_MAX_ENERGY;
	
	private int ALM_CURRENT_ENERGY = ALM_INITIAL_ENERGY;
	private int ALM_CURRENT_COOLDOWN = 0;
	private AlmightyAI almAI = new AlmightyAI();
	
	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}
		
		if (ALM_CURRENT_COOLDOWN == ALM_COOL_DOWN) { // cool time done
			ALM_CURRENT_COOLDOWN = 0;
			ALM_CURRENT_ENERGY--;
			if(ALM_CURRENT_ENERGY == 10)
				ALM_CURRENT_ENERGY = ALM_MAX_ENERGY;
			
			Command action = almAI.act(world, this);	
			if (action == null) { // if actor is unable to take any action
				System.out.println("unable to act!!");
			} else { // take action
				action.execute(world, this);
			}
		}
		else { // cool time not yet
			ALM_CURRENT_COOLDOWN++;
		}
	}

	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return AlM_VIEW_RANGE;
	}

	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return ALM_COOL_DOWN;
	}
	
	/**
	 * When number of fox exceeds rabbit, transform foxes into grass 
	 * inside viewRange.
	 * 
	 */
	public void Fox_to_Grass(World world, Location curLoc) {
		final int view = getViewRange();
		
		for(int x = curLoc.getX() - view; x <= curLoc.getX() + view; x++){
			for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++){
				Location temp = new Location(x,y);
				if(world.isValidLocation(temp)){
					if(world.getThing(temp) instanceof Fox){
						System.out.println("Change fox into Grass!!!");
						world.remove(world.getThing(temp));
						Grass grass = new Grass();
						world.add(grass, temp);
					}
				}
			}
		}
	}
	
	/**
	 * When number of rabbit exceeds grass, transform rabbits into grass
	 * inside viewRange. 
	 * 
	 */
	public void Rabbit_to_Grass(World world, Location curLoc) {
		final int view = getViewRange();
		
		for(int x = curLoc.getX() - view; x <= curLoc.getX() + view; x++){
			for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++){
				Location temp = new Location(x,y);
				if(world.isValidLocation(temp)){
					if(world.getThing(temp) instanceof Rabbit){
						System.out.println("Change Rabbit into Grass!!!");
						world.remove(world.getThing(temp));
						Grass grass = new Grass();
						world.add(grass, temp);
					}
				}
			}
		}
	}
	
	/**
	 * When number of grass exceeds 4 times of rabbit's number, 
	 * find a fox and transform grass into rabbits.
	 * If there is no fox in the world, make new fox.
	 * 
	 */
	public void Too_Many_Grass(World world, Location curLoc){
		Location foxLoc = null;
		final int view = getViewRange();
		
		for(int x = 0; x <= world.getWidth() + view; x++){
			for (int y = 0; y <= world.getHeight() + view; y++){
				foxLoc = new Location(x,y);
				if(world.isValidLocation(foxLoc)){
					if(world.getThing(foxLoc) instanceof Fox){
						break;
					}
				}
			}
			if(world.isValidLocation(foxLoc)){
				if(world.getThing(foxLoc) instanceof Fox)
					break;
			}
		}

		if (foxLoc != null) {
			for (int x = foxLoc.getX() - view; x <= foxLoc.getX() + view; x++) {
				for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++) {
					Location temp = new Location(x, y);
					if (world.isValidLocation(temp)) {
						if (world.getThing(temp) instanceof Grass) {
							System.out.println("Change Grass into Rabbit!!!");
							world.remove(world.getThing(temp));
							RabbitImpl rabbit = new RabbitImpl();
							world.add(rabbit, temp);
						}
					}
				}
			}
		}
		else{ // if there are no foxes, make new 2 foxes.
			FoxImpl newfox1 = new FoxImpl();
			FoxImpl newfox2 = new FoxImpl();
			world.add(newfox1, Util.randomEmptyLoc(world));
			world.add(newfox2, Util.randomEmptyLoc(world));
		}
	}
	
	/**
	 * Moves to random empty location.
	 */
	public void teleport(World world) {
		// TODO Auto-generated method stub
		Location dest = Util.randomEmptyLoc(world);

		Object temp = this;
		world.remove(this);
		world.add(temp, dest);
	}
	
	/**
	 * Almighty doesn't need any Edible to eat, but only one thing Almighty 
	 * hates is Gnat. Almighty eats Gnat when he finds it inside viewRange.
	 * Gnat's location doesn't need to be adjacent to be eaten.
	 */
	@Override
	public void eat(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location curLoc = world.getLocation(this);
		final int view = getViewRange();
		
		for(int x = curLoc.getX() - view; x <= curLoc.getX() + view; x++){
			for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++){
				Location temp = new Location(x,y);
				if(world.isValidLocation(temp)){
					if(world.getThing(temp) instanceof Gnat){
						world.remove(world.getThing(temp));
						return;
					}
				}
			}
		}
	}

	@Override
	public void move(World world, Direction dir) {
		// TODO Auto-generated method stub
	}

	@Override
	public void breed(World world, Direction dir) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return ALM_CURRENT_ENERGY;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return ALM_MAX_ENERGY;
	}

	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
