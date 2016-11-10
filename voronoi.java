import megamu.mesh.*;

// setup canvas size:
int WIDTH = 800;
int HEIGHT = 600;

size(800, 600);

//create array of points for voronoi diagram
float[][] points = new float[4][2];
//                       [rows][columns]

int voronoiCells = 40;
int randomX = 0;
int randomY = 0;
                                                   //  x       y
float[][] pointsTest = new float[voronoiCells][2]; //{ [0][0] [0][1] }, { [1][0] [1][1] }, ..., { [40][0] [40][1] }

// we fill these points with reasonable random values
loadPixels(); //pixels.length; pixels.width
for (int row = 0; row < voronoiCells; row++){
  for(int col = 0; col < 2; col++){
    //check if value is X or Y (X values will always have cols==0)
    if(col==0){
      randomX = int(random(200, (HEIGHT - 200))); //-100 to give slack on screen
      pointsTest[row][col] = randomX;
      print("X:"+randomX+" ");
    }
    else
    {
      randomY = int(random(250, (WIDTH - 250))); //-100 to give slack on screen
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
  
  strokeWeight(0.8);
  line(startX, startY, endX, endY); //draw
}

points[0][0] = 120; // first point, x
points[0][1] = 230; // first point, y

points[1][0] = 150; // second point, x
points[1][1] = 105; // second point, y

points[2][0] = 320; // third point, x
points[2][1] = 113; // third point, y

points[3][0] = 600;
points[3][1] = 420;

// save coloring info in vectors:
PVector f1, f2, f3;
f1 = new PVector(255, 0, 0); //fill 1
f2 = new PVector(0, 255, 0); //fill 2
f3 = new PVector(0, 0, 255); //fill 3

// create Voronoi object, pass array to it
Voronoi myVoronoi = new Voronoi(points);

// create two dimensional array for edges
float[][] myEdges = myVoronoi.getEdges();

//traverse edges

/*
for(int i=0; i<myEdges.length; i++)
{
  float startX = myEdges[i][0];
  float startY = myEdges[i][1];
  float endX = myEdges[i][2];
  float endY = myEdges[i][3];
  
  strokeWeight(1.5);
  line(startX, startY, endX, endY); //draw
}

// create array for voronoi regions
MPolygon[] myRegions = myVoronoi.getRegions();

//traverse regions
for(int i=0; i<myRegions.length; i++)
{
  // an array of points
  float[][] regionCoordinates = myRegions[i].getCoords();
  println(regionCoordinates[2][0]); //debug purposes

  if(i==0){fill(255,0,0,75);}
  if(i==1){fill(0,255,0,75);}
  if(i==2){fill(0,0,255,75);}
  myRegions[i].draw(this); // draw this shape
}

//draw each point on the voronoi diagram:
fill(f1.x,f1.y,f1.z,400);
stroke(f1.x,f1.y,f1.z,400);
ellipse(120, 230, 5, 5);

fill(f2.x,f2.y,f2.z,400);
stroke(f2.x,f2.y,f2.z,400);
ellipse(150, 105, 5, 5);

stroke(f3.x,f3.y,f3.z,400);
fill(f3.x,f3.y,f3.z,400);
ellipse(320,113, 5, 5);
*/