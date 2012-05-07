package net.kingsbery.games.pcg;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class LSystem {

  public static class StochasticLSystem extends LSystem {

    Random rnd = new Random(13);
    
    private static String ofLen(String x, int len){
      String result="";
      for(int i=0; i<len; i++){
        result+=x;
      }
      return result;
    }
    
    @Override
    protected String getTransition(char charAt) {
      if(charAt=='0'){
//        int turnSize=(int)(rnd.nextGaussian()*1+9);
        int turnSize=(int) ((rnd.nextFloat()>0.9) ?  rnd.nextGaussian()*1+9 : 9);
        
        String left=ofLen("<",turnSize);
        String right=ofLen(">",turnSize);
        return "B["+left+"0]"+right+"0";
      }else if(charAt=='B'){
        return ofLen("1",(int) (rnd.nextGaussian()*50+100));
      }else{
        throw new RuntimeException("Cannot process " + charAt);
      }
    }

    @Override
    protected boolean canTransition(char charAt) {
      return "0B".contains(new String(new char[]{charAt}));
    }

    @Override
    protected String getInit() {
      return "0";
    }

  }

  public static class MapLSystem extends LSystem {
    private String init;
    private Map<Character, String> transitions;

    public MapLSystem(String init, Map<Character, String> transitions) {
      this.init = init;
      this.transitions = transitions;
    }

    protected boolean canTransition(char charAt) {
      return transitions.containsKey(charAt);
    }

    protected String getTransition(char charAt) {
      return transitions.get(charAt);
    }

    protected String getInit() {
      return this.init;
    }

  }

  public static void main(String args[]) {
    LSystem lSystem = getPlantSystem();
    System.out.println(lSystem.eval(5));
    System.out.println(new StochasticLSystem().eval(5));
  }

  public static LSystem getPlantSystem() {
    String init = "0";
    Map<Character, String> transitions = new HashMap<Character, String>();
    transitions.put('1', "11");
    transitions.put('0', "1[<<<<<<<<<0]>>>>>>>>>0");
    LSystem lSystem = new MapLSystem(init, transitions);
    return lSystem;
  }

  private String val;

  public String eval(int iter) {
    if (val == null) {
      String line = getInit();
      for (int i = 0; i < iter; i++) {
        StringBuffer next = new StringBuffer();
        for (int j = 0; j < line.length(); j++) {
          char charAt = line.charAt(j);
          if (canTransition(charAt)) {
            next.append(getTransition(charAt));
          } else {
            next.append(charAt);
          }
        }
        line = next.toString();
      }
      val = line;
    }
    return val;
  }

  protected abstract String getTransition(char charAt);

  protected abstract boolean canTransition(char charAt);

  protected abstract String getInit();

}
