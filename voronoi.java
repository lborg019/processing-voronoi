import megamu.mesh.*;

//set voronoi constraint size:
int WIDTH = 640;
int HEIGHT = 480;

//setup canvas size:
size(800, 600);
background(255,255,255); //canvas background (white)
fill(170,199,243); //voronoi region background
rect(80, 60, WIDTH, HEIGHT); //center rectangle will constrain the voronoi diagram

// array of points for voronoi, where  x coord = [row], y coord = [column]
int voronoiCells = 40;
float[][] pointsTest = new float[voronoiCells][2]; //{ [0][0] [0][1] }, { [1][0] [1][1] }, ..., { [40][0] [40][1] }

// we fill these points with reasonable random values
int randomX = 0;
int randomY = 0;
for (int row = 0; row < voronoiCells; row++){
  for(int col = 0; col < 2; col++){
    //check if value is X or Y (X values will always have cols==0)
    if(col==0){ //WIDTH
      randomX = int(random((800-WIDTH), WIDTH)); //-100 to give slack on screen
      pointsTest[row][col] = randomX;
      print("X:"+randomX+" ");
    }
    else{ //HEIGHT
      randomY = int(random((600-HEIGHT), HEIGHT)); //-100 to give slack on screen
      pointsTest[row][col] = randomY;
      print("Y:"+randomY+"\n");
    }
    //check for variation
    //add checks to make sure points don't fall on same spot.
  }
}

//pass array of points to Voronoi object
Voronoi myVoronoiTest = new Voronoi(pointsTest);

// create array of edges
float[][] myEdgesTest = myVoronoiTest.getEdges();

//traverse edges
for (int i = 0; i < myEdgesTest.length; i++){
  
  float startX = myEdgesTest[i][0];
  float startY = myEdgesTest[i][1];
  float endX = myEdgesTest[i][2];
  float endY = myEdgesTest[i][3];
  
  stroke(136, 159,194);
  strokeWeight(1);
  line(startX, startY, endX, endY); //draw edge line
  //line([x1 y1], [x2 y2]) (from coord 1 to coord 2)
  
  stroke(255,0,0);
  fill(255,255,255);
  ellipse(startX, startY, 3, 3);//draw circles on top start vertices
  fill(255,0,255);
  ellipse(endX, endY, 3, 3);//draw circles on top start vertices
}
    
//prepare coloring information for different regions
PVector f1, f2, f3;
f1 = new PVector(255, 0, 0); //fill 1
f2 = new PVector(0, 255, 0); //fill 2
f3 = new PVector(0, 0, 255); //fill 3

//draw voronoi generation seed vertices
int xCoord = 0;
int yCoord = 0;
for (int row = 0; row < voronoiCells; row++){
  for(int col = 0; col < 2; col++){
    if(col==0) //WIDTH
      xCoord = (int)pointsTest[row][col];
    else //HEIGHT
    {
      yCoord = (int)pointsTest[row][col];
      
      //draw vertex:
      stroke(0,0,255);
      fill(0,0,255);
      ellipse(xCoord, yCoord, 1, 1);
    }
  }
}