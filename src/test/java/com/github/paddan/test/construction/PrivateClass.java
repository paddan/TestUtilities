//MIT License
//
//    Copyright (c) 2016 TestUtilities by Patrik Lindefors
//
//    Permission is hereby granted, free of charge, to any person obtaining a copy
//    of this software and associated documentation files (the "Software"), to deal
//    in the Software without restriction, including without limitation the rights
//    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//    copies of the Software, and to permit persons to whom the Software is
//    furnished to do so, subject to the following conditions:
//
//    The above copyright notice and this permission notice shall be included in all
//    copies or substantial portions of the Software.
//
//    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//    SOFTWARE.

package com.github.paddan.test.construction;

/**
 * 
 * @author patrik.lindefors
 *
 */
public final class PrivateClass {
    
    private String string;
    private Long number;

    private PrivateClass() {
        
    }
    
    private PrivateClass(String string, Long number) {
        this.string = string;
        this.number = number;
    }

    @SuppressWarnings("unused")
    private static String returnStringFromStatic() {
        return "hej";
    }
    
    @SuppressWarnings("unused")
    private static String returnStringFromStatic(String retVal) {
        return retVal;
    }

    @SuppressWarnings("unused")
    private String returnStringFromPrivate() {
        return "hej";
    }

    @SuppressWarnings("unused")
    private String returnStringFromPrivate(String retVal) {
        return retVal;
    }

    @SuppressWarnings("unused")
    private String getPrivateString() {
        return string;
    }

    public Long getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }
}
