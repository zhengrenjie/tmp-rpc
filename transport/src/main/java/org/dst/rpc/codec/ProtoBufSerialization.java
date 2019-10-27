package org.dst.rpc.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;
import java.io.IOException;
import java.lang.reflect.Method;


public class ProtoBufSerialization implements Serialization {

  @Override
  public byte[] serialize(Object object) throws IOException {
    byte[] result;
    if(object instanceof Builder) {
      object = ((Builder) object).build();
    }
    if(object instanceof Message) {
      result = ((Message) object).toByteArray();
    } else {
      throw new IllegalArgumentException("Serialize error, object must instanceof Message or Builder");
    }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
    GeneratedMessageV3.Builder builder;
    try {
      Method method = clazz.getMethod("newBuilder");
      builder = (GeneratedMessageV3.Builder) method.invoke(null, null);
    } catch (Exception e) {
      throw new IllegalArgumentException("Get google protobuf message builder from " + clazz.getName() + "failed", e);
    }
    builder.mergeFrom(bytes);
    return (T) builder.build();
  }
}
