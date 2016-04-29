/*

VR Instancing Progress:
- igByGeom gets huge, and updateInstances still happens on it,
  even if the associated geometry is no longer in the scene
  - track which InstanceGeometry to add or remove inside the Geometry?
  - have list of instances to render maintained somewhere else?

*/

package com.mycompany.escape_room;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
//import jmevr.app.VRApplication;
//import jmevr.input.OpenVR;
//import jmevr.input.VRBounds;
//import jmevr.input.VRInput;
//import jmevr.input.VRInput.VRINPUT_TYPE;
//import jmevr.post.CartoonSSAO;
//import jmevr.util.VRGuiManager;
//import jmevr.util.VRGuiManager.POSITIONING_MODE;

/**
 *
 * @author reden
 */
public class Main {
	
}