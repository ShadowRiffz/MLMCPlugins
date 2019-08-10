package io.github.bananapuncher714;

import com.google.common.primitives.Primitives;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class NBTEditor
{
  private static HashMap<String, Class<?>> classCache;
  private static HashMap<String, Method> methodCache;
  private static HashMap<Class<?>, Constructor<?>> constructorCache;
  private static HashMap<Class<?>, Class<?>> NBTClasses;
  private static HashMap<Class<?>, Field> NBTTagFieldCache;
  private static Field NBTListData;
  private static String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
  
  static
  {
    classCache = new HashMap();
    try
    {
      classCache.put("NBTBase", Class.forName("net.minecraft.server." + version + "." + "NBTBase"));
      classCache.put("NBTTagCompound", Class.forName("net.minecraft.server." + version + "." + "NBTTagCompound"));
      classCache.put("NBTTagList", Class.forName("net.minecraft.server." + version + "." + "NBTTagList"));
      classCache.put("NBTBase", Class.forName("net.minecraft.server." + version + "." + "NBTBase"));
      
      classCache.put("ItemStack", Class.forName("net.minecraft.server." + version + "." + "ItemStack"));
      classCache.put("CraftItemStack", Class.forName("org.bukkit.craftbukkit." + version + ".inventory." + "CraftItemStack"));
      
      classCache.put("Entity", Class.forName("net.minecraft.server." + version + "." + "Entity"));
      classCache.put("CraftEntity", Class.forName("org.bukkit.craftbukkit." + version + ".entity." + "CraftEntity"));
      classCache.put("EntityLiving", Class.forName("net.minecraft.server." + version + "." + "EntityLiving"));
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    NBTClasses = new HashMap();
    try
    {
      NBTClasses.put(Byte.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagByte"));
      NBTClasses.put(String.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagString"));
      NBTClasses.put(Double.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagDouble"));
      NBTClasses.put(Integer.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagInt"));
      NBTClasses.put(Long.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagLong"));
      NBTClasses.put(Short.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagShort"));
      NBTClasses.put(Float.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagFloat"));
      NBTClasses.put(Class.forName("[B"), Class.forName("net.minecraft.server." + version + "." + "NBTTagByteArray"));
      NBTClasses.put(Class.forName("[I"), Class.forName("net.minecraft.server." + version + "." + "NBTTagIntArray"));
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    methodCache = new HashMap();
    try
    {
      methodCache.put("get", getNMSClass("NBTTagCompound").getMethod("get", new Class[] { String.class }));
      methodCache.put("set", getNMSClass("NBTTagCompound").getMethod("set", new Class[] { String.class, getNMSClass("NBTBase") }));
      methodCache.put("hasKey", getNMSClass("NBTTagCompound").getMethod("hasKey", new Class[] { String.class }));
      methodCache.put("setIndex", getNMSClass("NBTTagList").getMethod("a", new Class[] { Integer.TYPE, getNMSClass("NBTBase") }));
      methodCache.put("add", getNMSClass("NBTTagList").getMethod("add", new Class[] { getNMSClass("NBTBase") }));
      
      methodCache.put("hasTag", getNMSClass("ItemStack").getMethod("hasTag", new Class[0]));
      methodCache.put("getTag", getNMSClass("ItemStack").getMethod("getTag", new Class[0]));
      methodCache.put("setTag", getNMSClass("ItemStack").getMethod("setTag", new Class[] { getNMSClass("NBTTagCompound") }));
      methodCache.put("asNMSCopy", getNMSClass("CraftItemStack").getMethod("asNMSCopy", new Class[] { ItemStack.class }));
      methodCache.put("asBukkitCopy", getNMSClass("CraftItemStack").getMethod("asBukkitCopy", new Class[] { getNMSClass("ItemStack") }));
      
      methodCache.put("getEntityHandle", getNMSClass("CraftEntity").getMethod("getHandle", new Class[0]));
      methodCache.put("getEntityTag", getNMSClass("Entity").getMethod("c", new Class[] { getNMSClass("NBTTagCompound") }));
      methodCache.put("setEntityTag", getNMSClass("Entity").getMethod("f", new Class[] { getNMSClass("NBTTagCompound") }));
      
      methodCache.put("getTileEntity", getNMSClass("CraftBlockState").getMethod("getTileEntity", new Class[0]));
      methodCache.put("setTileTag", getNMSClass("TileEntity").getMethod("a", new Class[] { getNMSClass("NBTTagCompound") }));
      methodCache.put("updateTile", getNMSClass("TileEntity").getMethod("update", new Class[0]));
      methodCache.put("udateState", getNMSClass("CraftBlockState").getMethod("update", new Class[] { Boolean.TYPE, Boolean.TYPE }));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    try
    {
      methodCache.put("getTileTag", getNMSClass("TileEntity").getMethod("save", new Class[] { getNMSClass("NBTTagCompound") }));
    }
    catch (NoSuchMethodException exception)
    {
      try
      {
        methodCache.put("getTileTag", getNMSClass("TileEntity").getMethod("b", new Class[] { getNMSClass("NBTTagCompound") }));
      }
      catch (Exception exception2)
      {
        exception2.printStackTrace();
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    constructorCache = new HashMap();
    try
    {
      constructorCache.put(getNBTTag(Byte.class), getNBTTag(Byte.class).getConstructor(new Class[] { Byte.TYPE }));
      constructorCache.put(getNBTTag(String.class), getNBTTag(String.class).getConstructor(new Class[] { String.class }));
      constructorCache.put(getNBTTag(Double.class), getNBTTag(Double.class).getConstructor(new Class[] { Double.TYPE }));
      constructorCache.put(getNBTTag(Integer.class), getNBTTag(Integer.class).getConstructor(new Class[] { Integer.TYPE }));
      constructorCache.put(getNBTTag(Long.class), getNBTTag(Long.class).getConstructor(new Class[] { Long.TYPE }));
      constructorCache.put(getNBTTag(Float.class), getNBTTag(Float.class).getConstructor(new Class[] { Float.TYPE }));
      constructorCache.put(getNBTTag(Short.class), getNBTTag(Short.class).getConstructor(new Class[] { Short.TYPE }));
      constructorCache.put(getNBTTag(Class.forName("[B")), getNBTTag(Class.forName("[B")).getConstructor(new Class[] { Class.forName("[B") }));
      constructorCache.put(getNBTTag(Class.forName("[I")), getNBTTag(Class.forName("[I")).getConstructor(new Class[] { Class.forName("[I") }));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    NBTTagFieldCache = new HashMap();
    try
    {
      for (Class<?> clazz : NBTClasses.values())
      {
        Field data = clazz.getDeclaredField("data");
        data.setAccessible(true);
        NBTTagFieldCache.put(clazz, data);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    try
    {
      NBTListData = getNMSClass("NBTTagList").getDeclaredField("list");
      NBTListData.setAccessible(true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public static Class<?> getPrimitiveClass(Class<?> clazz)
  {
    return Primitives.unwrap(clazz);
  }
  
  public static Class<?> getNBTTag(Class<?> primitiveType)
  {
    if (NBTClasses.containsKey(primitiveType)) {
      return (Class)NBTClasses.get(primitiveType);
    }
    return primitiveType;
  }
  
  public static Object getNBTVar(Object object)
  {
    if (object == null) {
      return null;
    }
    Class<?> clazz = object.getClass();
    try
    {
      if (NBTTagFieldCache.containsKey(clazz)) {
        return ((Field)NBTTagFieldCache.get(clazz)).get(object);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    return null;
  }
  
  public static Method getMethod(String name)
  {
    return methodCache.containsKey(name) ? (Method)methodCache.get(name) : null;
  }
  
  public static Constructor<?> getConstructor(Class<?> clazz)
  {
    return constructorCache.containsKey(clazz) ? (Constructor)constructorCache.get(clazz) : null;
  }
  
  public static Class<?> getNMSClass(String name)
  {
    if (classCache.containsKey(name)) {
      return (Class)classCache.get(name);
    }
    try
    {
      return Class.forName("net.minecraft.server." + version + "." + name);
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static Object getItemTag(ItemStack item, Object... keys)
  {
    try
    {
      Object stack = null;
      stack = getMethod("asNMSCopy").invoke(null, new Object[] { item });
      
      Object tag = null;
      if (getMethod("hasTag").invoke(stack, new Object[0]).equals(Boolean.valueOf(true))) {
        tag = getMethod("getTag").invoke(stack, new Object[0]);
      } else {
        tag = getNMSClass("NBTTagCompound").newInstance();
      }
      return getTag(tag, keys);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    return null;
  }
  
  public static ItemStack setItemTag(ItemStack item, Object value, Object... keys)
  {
    try
    {
      Object stack = getMethod("asNMSCopy").invoke(null, new Object[] { item });
      
      Object tag = null;
      if (getMethod("hasTag").invoke(stack, new Object[0]).equals(Boolean.valueOf(true))) {
        tag = getMethod("getTag").invoke(stack, new Object[0]);
      } else {
        tag = getNMSClass("NBTTagCompound").newInstance();
      }
      setTag(tag, value, keys);
      
      getMethod("setTag").invoke(stack, new Object[] { tag });
      return (ItemStack)getMethod("asBukkitCopy").invoke(null, new Object[] { stack });
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    return null;
  }
  
  public static Object getEntityTag(Entity entity, Object... keys)
  {
    try
    {
      Object NMSEntity = getMethod("getEntityHandle").invoke(entity, new Object[0]);
      
      Object tag = getNMSClass("NBTTagCompound").newInstance();
      
      getMethod("getEntityTag").invoke(NMSEntity, new Object[] { tag });
      
      return getTag(tag, keys);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    return null;
  }
  
  public static void setEntityTag(Entity entity, Object value, Object... keys)
  {
    try
    {
      Object NMSEntity = getMethod("getEntityHandle").invoke(entity, new Object[0]);
      
      Object tag = getNMSClass("NBTTagCompound").newInstance();
      
      getMethod("getEntityTag").invoke(NMSEntity, new Object[] { tag });
      
      setTag(tag, value, keys);
      
      getMethod("setEntityTag").invoke(NMSEntity, new Object[] { tag });
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      return;
    }
  }
  
  private static void setTag(Object tag, Object value, Object... keys)
    throws Exception
  {
    Object notCompound = getConstructor(getNBTTag(value.getClass())).newInstance(new Object[] { value });
    
    Object compound = tag;
    for (int index = 0; index < keys.length; index++)
    {
      Object key = keys[index];
      if (index + 1 == keys.length)
      {
        if (key == null)
        {
          getMethod("add").invoke(compound, new Object[] { notCompound });
          break;
        }
        if ((key instanceof Integer))
        {
          getMethod("setIndex").invoke(compound, new Object[] { Integer.valueOf(((Integer)key).intValue()), notCompound });
          break;
        }
        getMethod("set").invoke(compound, new Object[] { (String)key, notCompound });
        
        break;
      }
      Object oldCompound = compound;
      if ((key instanceof Integer)) {
        compound = ((List)NBTListData.get(compound)).get(((Integer)key).intValue());
      } else if (key != null) {
        compound = getMethod("get").invoke(compound, new Object[] { (String)key });
      }
      if ((compound == null) || (key == null))
      {
        if ((keys[(index + 1)] == null) || ((keys[(index + 1)] instanceof Integer))) {
          compound = getNMSClass("NBTTagList").newInstance();
        } else {
          compound = getNMSClass("NBTTagCompound").newInstance();
        }
        if (oldCompound.getClass().getSimpleName().equals("NBTTagList")) {
          getMethod("add").invoke(oldCompound, new Object[] { compound });
        } else {
          getMethod("set").invoke(oldCompound, new Object[] { (String)key, compound });
        }
      }
    }
  }
  
  private static Object getTag(Object tag, Object... keys)
    throws Exception
  {
    Object notCompound = tag;
    Object[] arrayOfObject;
    int j = (arrayOfObject = keys).length;
    for (int i = 0; i < j; i++)
    {
      Object key = arrayOfObject[i];
      if (notCompound == null) {
        return null;
      }
      if (notCompound.getClass().getSimpleName().equals("NBTTagCompound")) {
        notCompound = getMethod("get").invoke(notCompound, new Object[] { (String)key });
      } else if (notCompound.getClass().getSimpleName().equals("NBTTagList")) {
        notCompound = ((List)NBTListData.get(notCompound)).get(((Integer)key).intValue());
      } else {
        return getNBTVar(notCompound);
      }
    }
    if (notCompound == null) {
      return null;
    }
    if (notCompound.getClass().getSimpleName().equals("NBTTagList")) {
      return Integer.valueOf(((List)NBTListData.get(notCompound)).size());
    }
    return getNBTVar(notCompound);
  }
}
