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
  void display(){
    noStroke();
    colorMode(HSB);
    float hue = map(v.mag(), 0, 5, 0, 255 );
    fill(hue,255,200);
    ellipse(loc.x ,loc.y, size ,size);
  }
  void update(boolean check)  //Whether to check edges or not
  {
    
    v.add(acc);
    checkEdges(check);
    
    v.limit(5);
    loc.add(v);
    acc.mult(0);
  }
  void checkEdges(boolean random){  //Whether to randomize or not
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
  void run(){
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
  void applyForce(PVector force){
    
    for(int i = p.size()-1; i >=0; --i){
      Particle pl = p.get(i);
      pl.acc.add(force);
    }
  }
  void attract(PVector d){
    int num = 0;
    if(mode == 1){
      num = 1;
    }
    for(int i = p.size() -1; i >= num; --i){
      Particle pl = p.get(i);
      PVector dir = PVector.sub(d,pl.loc);
      float dist = dir.mag();
      dir.normalize();
      dir.mult(0.3);
      if(dist < 150){
        dir.normalize();
        dir.mult(-1.5);
      }
      pl.acc = dir;
   }
  }
  void implement_coupling(){
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
        dir.mult(0.5);
        
      p2.acc = dir;  
      }
      else{
        p2.acc = p1.acc;
      }    
    }
  }
}
