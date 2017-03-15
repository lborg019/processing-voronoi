
    /**
     * This demo shows the basic usage pattern of the Voronoi class in combination with
     * the SutherlandHodgeman polygon clipper to constrain the resulting shapes.
     *
     * Usage:
     * mouse click: add point to voronoi
     * p: toggle points
     * t: toggle triangles
     * x: clear all
     * r: add random
     * c: toggle clipping
     * h: toggle help display
     * space: save frame
     *
     * Voronoi class ported from original code by L. Paul Chew
     */

    /* 
     * Copyright (c) 2010 Karsten Schmidt
     * 
     * This demo & library is free software; you can redistribute it and/or
     * modify it under the terms of the GNU Lesser General Public
     * License as published by the Free Software Foundation; either
     * version 2.1 of the License, or (at your option) any later version.
     * 
     * http://creativecommons.org/licenses/LGPL/2.1/
     * 
     * This library is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
     * Lesser General Public License for more details.
     * 
     * You should have received a copy of the GNU Lesser General Public
     * License along with this library; if not, write to the Free Software
     * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
     */
    import toxi.geom.*;
    import toxi.geom.mesh2d.*;

    import toxi.util.*;
    import toxi.util.datatypes.*;

    import toxi.processing.*;

    // ranges for x/y positions of points
    FloatRange xpos, ypos;

    // helper class for rendering
    ToxiclibsSupport gfx;

    // empty voronoi mesh container
    Voronoi voronoi = new Voronoi();

    // optional polygon clipper
    PolygonClipper2D clip;
    
    //int convergence=0;

    // switches
    boolean doShowPoints = true;
    boolean doShowDelaunay;
    boolean doShowHelp=true;
    boolean doClip;
    boolean doSave;
    boolean doShowCentroids=false;

    void setup() {
      size(600, 600);
      smooth();
      // focus x positions around horizontal center (w/ 33% standard deviation)
      xpos=new BiasedFloatRange(0, width, width/2, 0.333f);
      // focus y positions around bottom (w/ 50% standard deviation)
      ypos=new BiasedFloatRange(0, height, height, 0.5f);
      // setup clipper with centered rectangle
      clip=new SutherlandHodgemanClipper(new Rect(width*0.125, height*0.125, width*0.75, height*0.75));
      gfx = new ToxiclibsSupport(this);
      textFont(createFont("SansSerif", 10));
    }

    void draw()
    {
      background(255);
      stroke(0);
      //frameRate(60);
      noLoop();
      noFill();
      // draw all voronoi polygons, clip them if needed...
      for (Polygon2D poly : voronoi.getRegions()) {
        if (doClip) {
          gfx.polygon2D(clip.clipPolygon(poly));
        } 
        else {
          gfx.polygon2D(poly);
        }
      }
      // draw delaunay triangulation
      if (doShowDelaunay) {
        stroke(0, 0, 255, 50);
        beginShape(TRIANGLES);
        for (Triangle2D t : voronoi.getTriangles()) {
          gfx.triangle(t, false);
        }
        endShape();
      }
      // draw original points added to voronoi
      if (doShowPoints) {
        fill(255, 0, 255);
        noStroke();
        for (Vec2D c : voronoi.getSites()) {
          ellipse(c.x, c.y, 5, 5);
        }
      }
      if (doSave) {
        saveFrame("voronoi-" + DateUtils.timeStamp() + ".png");
        doSave = false;
      }
      
      //draw centroid
      if (doShowCentroids){
        fill(10,186,181);
        noStroke();
        Vec2D centroid = null;
        if(doClip)
        {
          for (Polygon2D poly : voronoi.getRegions()) {
            centroid=clip.clipPolygon(poly).getCentroid();
            ellipse(centroid.x, centroid.y, 5,5);
          }
        }
        else
        {
          for (Polygon2D poly : voronoi.getRegions()) {
            centroid=poly.getCentroid();
            ellipse(centroid.x, centroid.y, 5,5);
          }
        }
      }
      
      if (doShowHelp) {
        fill(255, 0, 0);
        text("p: toggle points", 20, 20);
        text("t: toggle triangles", 20, 40);
        text("e: toggle centroids", 20, 60);
        text("l: lloyd relax", 20, 80);
        text("x: clear all", 20, 100);
        text("r: add random", 20, 120);
        text("c: toggle clipping", 20, 140);
        text("h: toggle help display", 20, 160);
        text("space: save frame", 20, 180);
      }
    } // end draw

    // lloydRelax() function will return the disparity between
    // every coordinate point from voronoi graph and its
    // respective centroid
    
    //boolean lloydRelax(){
    void lloydRelax(){
        println("lloyd step");
        // Vec2D centroid = null;
        ArrayList<Vec2D> centroids = new ArrayList<Vec2D>();
        
        // catch all centroids and save them in memory:
        for (Polygon2D poly : voronoi.getRegions()){
          centroids.add(clip.clipPolygon(poly).getCentroid());
        }
        // clear current voronoi
        voronoi = new Voronoi();
        
        // draw new voronoi graph
        for(int i=0; i < centroids.size(); i++)
          voronoi.addPoint(new Vec2D(centroids.get(i).x, centroids.get(i).y));
        /*  
        // calculate difference between current coordinates and its centroids:
        // at this point, centroids array contains all the coordinates
        ArrayList<Vec2D> coordinates = new ArrayList<Vec2D>();
        // copy:
        for(int i=0; i < centroids.size(); i++)
        {
          coordinates.add(new Vec2D(centroids.get(i).x, centroids.get(i).y));
        }
        
        // clear old centroids:
        centroids.clear();
        
        // calculate centroids for new coordinates and save in memory:
        for (Polygon2D poly : voronoi.getRegions()){
          centroids.add(clip.clipPolygon(poly).getCentroid());
        }
        
        // calculate the 2D-space (X,Y) difference between each coordinate and centroid:
        int flag=0; // ready
        for(int i=0; i < centroids.size(); i++)
        {
          int ceX=(int)centroids.get(i).x; // x value of centroid
          int ceY=(int)centroids.get(i).y; // y value of centroid
          
          int coX=(int)coordinates.get(i).x; // x value of coordinate
          int coY=(int)coordinates.get(i).y; // y value of coordinate
          
          println("ceX: "+ceX+"   ceY: "+ceY);
          println("cox: "+coX+"   coY: "+coY);
          
          int difX=ceX-coX;
          int difY=ceY-coY;
          
          println("diX: "+difX+"     diY: "+difY);
          println();
          
          if(abs(difX) > 1 || abs(difY) > 1) // more than 5 pixels discrepancy
          {
            flag=1; // not ready
          }
        }
        if (flag==0)
          return true;
        
        return false;*/
    }

    void keyPressed() {
      switch(key) {
      case ' ':
        doSave = true;
        loop();
        break;
      case 'l':
      
        // perform lloyd relaxation steps until convergence
        // convergence happens when coordinates closely match
        // calculated centroids with up until 5 pixel
        // discrepancy for each coordinate
        // * will only work with constrained area
        
        // clip diagram before relaxing:
        int flag=0;
        if(!doClip){
          doClip=true;
          loop();
          flag=1;
        }
          
        for(int i=0; i<=100; i++) // 100 steps
        //while(!lloydRelax())
        {
          lloydRelax();
          loop();
          delay(35);
        }
        
        if(flag==1){
          doClip=false;
          loop();
        }
        break;
      case 'e':
        doShowCentroids = !doShowCentroids;
        loop();
        break;
      case 't':
        doShowDelaunay = !doShowDelaunay;
        loop();
        break;
      case 'x':
        voronoi = new Voronoi();
        loop();
        break;
      case 'p':
        doShowPoints = !doShowPoints;
        loop();
        break;
      case 'c':
        doClip=!doClip;
        loop();
        break;
      case 'h':
        doShowHelp=!doShowHelp;
        loop();
        break;
      case 'r':
        for (int i = 0; i < 10; i++) {
          voronoi.addPoint(new Vec2D(xpos.pickRandom(), ypos.pickRandom()));
        }
        loop();
        break;
      }
    }

    void mousePressed() {
      loop();
      voronoi.addPoint(new Vec2D(mouseX, mouseY));
    }