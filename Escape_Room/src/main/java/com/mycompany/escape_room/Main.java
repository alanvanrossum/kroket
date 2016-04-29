
/*

VR Instancing Progress:
- igByGeom gets huge, and updateInstances still happens on it,
  even if the associated geometry is no longer in the scene
  - track which InstanceGeometry to add or remove inside the Geometry?
  - have list of instances to render maintained somewhere else?

*/


package com.mycompany.escape_room;


//import jmevr.app.VRApplication;


/**
 *
 * @author reden
 */
/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
