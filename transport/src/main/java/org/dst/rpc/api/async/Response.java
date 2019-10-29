package org.dst.rpc.api.async;


/**
 * @author zrj CreateDate: 2019/10/29
 */
public interface Response {

  long getRequestId();

  void setRequestId(long requestId);

  Object getValue();

  void setValue(Object value);

  Exception getException();

  void setException(Exception e);

  /**
   * has attribute.
   *
   * @param key key.
   * @return has or has not.
   */
  boolean hasAttribute(String key);

  /**
   * get attribute.
   *
   * @param key key.
   * @return value.
   */
  Object getAttribute(String key);

  /**
   * set attribute.
   *
   * @param key   key.
   * @param value value.
   */
  void setAttribute(String key, Object value);

  /**
   * remove attribute.
   *
   * @param key key.
   */
  void removeAttribute(String key);

}
