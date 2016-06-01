//package nl.tudelft.kroket.input.interaction;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.jme3.bounding.BoundingVolume;
//import com.jme3.collision.CollisionResults;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.shape.Sphere;
//
//import nl.tudelft.kroket.input.InteractionHandler;
//
//public class CollisionHandler extends InteractionHandler {
//
//  private boolean restrictObserver;
//
//  private Vector3f boundaries;
//
//  float collisionThreshold = 3.2f;
//  float collisionOffset = 8.0f;
//
//  private List<Spatial> objectList;
//
//  public CollisionHandler(Spatial observer) {
//    super(observer);
//
//    this.objectList = new ArrayList<Spatial>();
//
//    enableRestriction();
//  }
//
//  public void addObject(Spatial object) {
//    if (object == null)
//      return;
//
//    System.out.println("Adding collision object: " + object.getName());
//    objectList.add(object);
//  }
//
//  public void removeObject(Spatial object) {
//    if (object == null)
//      return;
//
//    objectList.remove(object);
//  }
//
//  private void handleObjectCollision(Spatial object, float collisionThreshold, float tpf) {
//
//    float deltaCorrected = collisionOffset * tpf;
//    
//
//    if (object.getWorldBound().intersects(observer.getWorldBound())) {
//      System.out.println("Player tried to move into " + object.getName());
//      observer.move(-deltaCorrected, 0, 0);
//    }
//     //   .contains(observer.getLocalTranslation().add(collisionThreshold, 0, 0))) {
//     // System.out.println("Player tried to move into " + object.getName());
//     // observer.move(-deltaCorrected, 0, 0);
//   // }
//
//    // collide X
//    // if (observer.getLocalTranslation().getX() + collisionThreshold <
//    // -object.getLocalTranslation().getX()
//    // + collisionThreshold) {
//    // observer.move(deltaCorrected, 0, 0);
//    // }
//    // if (observer.getLocalTranslation().getX() > -object.getLocalTranslation().getX()
//    // + collisionThreshold) {
//    // observer.move(deltaCorrected, 0, 0);
//    // }
//    // // collideZ
//    // if (observer.getLocalTranslation().getZ() > boundaries.getZ() - collisionThreshold) {
//    // observer.move(0, 0, -deltaCorrected);
//    // }
//    // if (observer.getLocalTranslation().getZ() < -boundaries.getZ() + collisionThreshold) {
//    // observer.move(0, 0, deltaCorrected);
//    // }
//    // // roof
//    // if (observer.getLocalTranslation().getY() > boundaries.getY()) {
//    // observer.move(0, -deltaCorrected, 0);
//    // }
//    // // floor
//    // if (observer.getLocalTranslation().getY() < 0) {
//    // observer.move(0, deltaCorrected, 0);
//    // }
//  }
//
//  public void enableRestriction() {
//    setRestriction(true);
//  }
//
//  public void disableRestriction() {
//    setRestriction(false);
//  }
//
//  public void setRestriction(boolean restrict) {
//    this.restrictObserver = restrict;
//  }
//
//  @Override
//  public void update(float tpf) {
//
//    if (restrictObserver) {
//
//      for (Spatial object : objectList) {
//        if (object == null)
//          continue;
//
//        handleObjectCollision(object, collisionThreshold, tpf);
//      }
//    }
//  }
//
//}
