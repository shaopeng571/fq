package com.huayang.utils.encode;
public interface IByteEncoder {
    String encode(byte[] bytes);

    byte[] decode(String str);
}
