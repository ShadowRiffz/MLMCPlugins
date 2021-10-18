package me.Neoblade298.NeoConsumables;

public class AttributeEffect
{
  String name;
  int amp;
  int dur;
  
  public AttributeEffect(String attr, int amp, int dur)
  {
    this.name = attr;
    this.amp = amp;
    this.dur = dur;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getAmp()
  {
    return this.amp;
  }
  
  public int getDuration()
  {
    return this.dur;
  }
}
