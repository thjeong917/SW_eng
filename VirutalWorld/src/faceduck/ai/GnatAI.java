package faceduck.ai;

import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

/**
 * The AI for a Gnat. This AI will pick a random direction and then return a
 * command which moves in that direction.
 *
 * This class serves as a simple example for how other AIs should be
 * implemented.
 */
public class GnatAI extends AbstractAI implements AI {
	/*
	 * Your AI implementation must provide a public default constructor so that
	 * the it can be initialized outside of the package.
	 */
	public GnatAI() {	
	}

	/*
	 * GnatAI is dumb. It disregards its surroundings and simply tells the Gnat
	 * to move in a random direction.
	 */
	@Override
	public Command act(World world, Actor actor) {
		Location loc = world.getLocation(actor);
		
		while (true) {
			Direction dir = Util.randomDir();
			// seed is consistent, so this might result same even if random
			Location dest = new Location(loc, dir);
			if (world.isValidLocation(dest)) {
				if (world.getThing(dest) == null) {
					MoveCommand move = new MoveCommand(dir);
					return move;
				}

				// if gnat is surrounded by objects in every direction
				else if (!abletoMove(world,loc)) {
					System.out.println("Nowhere to move");
					break;
				}
			}
		}
		
		return null;
	}
}
