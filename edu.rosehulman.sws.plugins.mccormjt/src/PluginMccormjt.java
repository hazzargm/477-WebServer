import edu.rosehulman.sws.extension.AbstractPlugin;
import edu.rosehulman.sws.extension.IPlugin;
import edu.rosehulman.sws.extension.User;

/*
 * mccormjt.java
 * May 3, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */
 

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class PluginMccormjt extends AbstractPlugin implements IPlugin {

	public PluginMccormjt() {
		super();
		this.domain = "mccormjt";
		initUserDatabase();
	}
	
	private void initUserDatabase() {
		User[] userList = new User[]{
			new User("John", 22),
			new User("Gordon", 23),
			new User("Chandan", 35),
			new User("SteveJobs", 66)
		};
		
		for (User u : userList) {
			users.put(u.getId(), u);
		}
	}
}
