package Helloserver;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import MeteoApp.Meteo;
import MeteoApp.MeteoHelper;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


public class ServerMeteo {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		try {
			//Create and initialize the ORB // get reference to rootpoa &am; activate the POAManager

			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "9999");
			ORB orb = ORB.init(args, props);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// Create servant and register it with ORB
			ServantMeteo meteoobj = new ServantMeteo();
			meteoobj.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(meteoobj);
			Meteo href = MeteoHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("ABC");
			ncRef.rebind(path, href);

			System.out.println("Hello Server ready and waiting...");

			// wait for invocations from clients

			for(;;) {
				orb.run();

			}

		}
		catch (Exception e){
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);


		}

	}
//tnameserv -ORBInitialPort 9999
}
