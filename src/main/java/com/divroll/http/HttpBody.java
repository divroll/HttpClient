/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.divroll.http;

import com.divroll.http.exception.CannotCastValueException;

public abstract class HttpBody<T> {
  protected final T value;

  /**
   * Create a new HttpBody
   *
   * @param value the value of this HttpBody
   */
  public HttpBody(T value) {
    this.value = value;
  }

  /**
   * Get the stored value
   *
   * @return the value of this HttpBody
   */
  public T getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpBody httpBody = (HttpBody) o;

    return value == null ? httpBody.value == null : value.equals(httpBody.value);
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public String toString() {
    return (value == null ? "null" : value.toString());
  }

  public <U extends HttpBody> boolean isA(Class<U> cls) {
    return cls.isAssignableFrom(this.getClass());
  }

  public <U extends HttpBody> U asA(Class<U> cls) throws CannotCastValueException {
    if (isA(cls)) {
      return cls.cast(this);
    } else {
      throw new CannotCastValueException(cls.getCanonicalName(),
          this.getClass().getCanonicalName());
    }
  }
}
