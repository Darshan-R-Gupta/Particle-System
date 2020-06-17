import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Particle_system extends PApplet {

ParticleSystem p;
//1 for following only the first particle
//2 for moving in couples 
//<0 for false
public void setup(){
  
  p = new ParticleSystem(new PVector(width/2, height/2) );
}
PVector gravity = new PVector(0,0);
public void draw()
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
public void mouseReleased(){
  p.applyForce(new PVector( random(-0.5f,0.5f) , random(-0.5f,0.5f) ) );
}
public void keyPressed(){
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
class Particle
{
  PVector loc;
  PVector acc;
  PVector v;
  int size;
  Particle(PVector l){
    loc = l;
    acc = new PVector();
    v = new PVector();
    size = 15;  
  }
  public void display(){
    noStroke();
    colorMode(HSB);
    float hue = map(v.mag(), 0, 5, 0, 255 );
    fill(hue,255,200);
    ellipse(loc.x ,loc.y, size ,size);
  }
  public void update(boolean check)  //Whether to check edges or not
  {
    
    v.add(acc);
    checkEdges(check);
    
    v.limit(5);
    loc.add(v);
    acc.mult(0);
  }
  public void checkEdges(boolean random){  //Whether to randomize or not
    if(loc.x + size/2 > width){
      loc.x = width - size/2;
     if(random){
       v.x = random(-5,0);  
        v.y = random(-5,5);
     }
     else{
       v.x *= -1;
     }
    }
    if(loc.x - size/2 <=0 ){
      loc.x = size/2;
      if(random){
        v.x = random(0,5);  
        v.y = random(-5,5);  
      }
      else{
        v.x *=-1;
      }
    }
    if(loc.y + size/2 > height)
    {
      // in case gravity attracts it
      loc.y = height - size/2;
      if(random){
        v.x = random(-5,5);
        v.y = random(-5,0);
      }
      else{
        v.y *= -1;
      }
    }
    if(loc.y - size/2 <=0){
       loc.y = size/2;
       if(random){
         v.x = random(-5,5);  
         v.y = random(0,5);  
       }
       else{
         v.y *= -1;
       }
    }
  }
};
class ParticleSystem
{
  ArrayList<Particle> p;
  PVector origin;
  int mode;
  ParticleSystem(PVector o){
    p = new ArrayList<Particle>();
    origin = o;
    mode = -1; //no mode
    for(int i=0; i < 200; ++i){
    
      PVector r = new PVector(random(-50,50), random(-50,50));
      PVector loc = PVector.sub(origin, r);
      p.add(new Particle(loc) );
      Particle pl = p.get(i);
      pl.acc = new PVector(random(-5,5), random(-5,5));
      
    }
  }
  public void run(){
    for(int i = p.size() -1; i >= 0; --i){
      Particle pl = p.get(i);
      if(mode != 2){
        pl.update(true);  //Update with boundary check
      }
      else{
         if(i%2 != 0){
           pl.update(true);
         }
         else{
           pl.update(false);
         }
      }
      pl.display();
    }
  } 
  public void applyForce(PVector force){
    
    for(int i = p.size()-1; i >=0; --i){
      Particle pl = p.get(i);
      pl.acc.add(force);
    }
  }
  public void attract(PVector d){
    int num = 0;
    if(mode == 1){
      num = 1;
    }
    for(int i = p.size() -1; i >= num; --i){
      Particle pl = p.get(i);
      PVector dir = PVector.sub(d,pl.loc);
      float dist = dir.mag();
      dir.normalize();
      dir.mult(0.3f);
      if(dist < 150){
        dir.normalize();
        dir.mult(-1.5f);
      }
      pl.acc = dir;
   }
  }
  public void implement_coupling(){
    text("COUPLE",20,20);
    Particle p1,p2; //1 means the head and 2 means the tail
    PVector dir;
    float dist;
    for(int i= p.size()-1;  i >=0 ; i-=2){
      p1 = p.get(i-1);
      p2 = p.get(i);
      dir = PVector.sub(p1.loc,p2.loc);
      dist = dir.mag();
      dir.normalize();
      if(dist > 30){
        dir.mult(0.5f);
        
      p2.acc = dir;  
      }
      else{
        p2.acc = p1.acc;
      }    
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Particle_system" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
