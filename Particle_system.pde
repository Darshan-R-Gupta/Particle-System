ParticleSystem p;
//1 for following only the first particle
//2 for moving in couples 
//<0 for false
void setup(){
  fullScreen();
  p = new ParticleSystem(new PVector(width/2, height/2) );
}
PVector gravity = new PVector(0,0);
void draw()
{
  background(0);
  p.applyForce(gravity);
  if(p.mode == 1){
    Particle pl = p.p.get(0);
    p.mode = 1;
    p.attract(pl.loc);  // the 2nd parameter informs the function whether it is follower mode or not
  }
  else if(p.mode == 2){
    p.implement_coupling();
  }
  p.run();
  
  if(mousePressed){
     PVector loc = new PVector(mouseX, mouseY);
     p.attract(loc);
  }
}
void mouseReleased(){
  p.applyForce(new PVector( random(-0.5,0.5) , random(-0.5,0.5) ) );
}
void keyPressed(){
  if(key == 'f' || key == 'F'){
     if(p.mode == 1){
        p.mode = -1;
     }
     else{
       p.mode = 1;
     }
  }
  else if(key == 'c' || key == 'C'){
    if(p.mode == 2){
      p.mode = -2;
    }
    else{
      p.mode =2;
    }
  }
}
