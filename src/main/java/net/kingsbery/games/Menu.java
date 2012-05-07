package net.kingsbery.games;

import java.util.List;
import java.util.Stack;

import net.kingsbery.games.Menu.CounterFrameCallback;
import net.kingsbery.games.menu.MenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Menu {

  private static final Log log = LogFactory.getLog(Menu.class);
  
  Stack<Frame> stack = new Stack<Frame>();

  private static abstract class Frame{

    public abstract void dec();
    public abstract void inc() ;

    public abstract int getCurrent();
    public abstract void enter() ;
  }
  
  private static class MenuFrame extends Frame {
    private int current;
    private List<MenuItem> options;

    public MenuFrame(List<MenuItem> options, int current) {
      this.options = options;
      this.current = current;
    }
    
    public String toString(){
      return "Frame <"+options.toString()+">";
    }

    @Override
    public void dec() {
      current = (current - 1) % options.size();
      if (current < 0) {
        current = options.size() + current;
      }      
    }

    @Override
    public void inc() {
      current = (current + 1) % options.size();      
    }

    @Override
    public int getCurrent() {
      return this.current;
    }

    @Override
    public void enter() {
        options.get(this.getCurrent()).action();
    }
  }
  
  public static interface CounterFrameCallback{
    public void handle(int x);
  }
  
  private static class CounterFrame extends Frame{
    int current;
    private CounterFrameCallback callback;
    private int max;
    private int min;

    public CounterFrame(CounterFrameCallback callback, int max, int min){
      this.callback=callback;
      this.max=max;
      this.min=min;
    }
    
    /**
     * These are backwards intentionally.
     */
    @Override
    public void dec() {
      if(current<max){
        current++;
      }
    }

    @Override
    public void inc() {
      if(current>min){
        current--;
      }
    }

    @Override
    public int getCurrent() {
      return this.current;
    }

    @Override
    public void enter() {
      callback.handle(current);
    }
  }
  


  public void dec() {
    Frame frame = this.stack.peek();
    frame.dec();
    
  }

  public void inc() {
    Frame frame = this.stack.peek();
    frame.inc();
  }

  public List<MenuItem> getOptions() {
    if(hasOptions()){
      return ((MenuFrame)this.stack.peek()).options;
    }else{
      throw new UnsupportedOperationException();
    }
  }
  
  public boolean stillGoing(){
    return !this.stack.isEmpty();
  }

  public int getCurrent() {
    return this.stack.peek().getCurrent();
  }

  /**
   * @param values
   */
  public void push(List<MenuItem> values) {
    this.stack.push(new MenuFrame(values,0));
  }
  
  public void pushCounter(CounterFrameCallback callback, int max, int min){
    this.stack.push(new CounterFrame(callback,max,min));
  }


  public void pop() {
    Frame frame = stack.pop();
    log.debug(frame);
    
  }

  public void enter() {
    stack.peek().enter();
  }

  public int getValue(){
    return stack.peek().getCurrent();
  }

  public boolean hasOptions() {
    return this.stack.peek() instanceof MenuFrame;
  }

  public boolean isCounter() {
    return this.stack.peek() instanceof CounterFrame;
  }

}
