/*
* Copyright (c) 2005, Graph Builder
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* * Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* * Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* * Neither the name of Graph Builder nor the names of its contributors may be
* used to endorse or promote products derived from this software without
* specific prior written permission.

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.graphbuilder.struc;

/**
Bag is a container of objects using an array.  The Bag is designed to be as light weight as possible.
It only contains a reference to an array and a size counter.  In methods that accept both an Object and
an int as parameters, the Object is always specified first.
*/
public class Bag {

	protected Object[] data = null;
	protected int size = 0;

	public Bag() {
		data = new Object[2];
	}

	public Bag(int initialCapacity) {
		data = new Object[initialCapacity];
	}

	public Bag(Object[] data, int size) {
		if (data == null)
			throw new IllegalArgumentException("data array cannot be null.");

		if (size < 0 || size > data.length)
			throw new IllegalArgumentException("required: (size >= 0 && size <= data.length) but: (size = " + size + ", data.length = " + data.length + ")");

		this.data = data;
		this.size = size;
	}

	public void add(Object o) {
		insert(o, size);
	}

	public int size() {
		return size;
	}

	public void setSize(int s) {
		if (s < 0 || s > data.length)
			throw new IllegalArgumentException("required: (size >= 0 && size <= data.length) but: (size = " + size + ", data.length = " + data.length + ")");

		size = s;
	}

	public void insert(Object o, int index) {
		if (index < 0 || index > size)
			throw new IllegalArgumentException("required: (index >= 0 && index <= size) but: (index = " + index + ", size = " + size + ")");

		ensureCapacity(size + 1);

		for (int i = size; i > index; i--)
			data[i] = data[i - 1];

		data[index] = o;
		size++;
	}

	public void ensureCapacity(int capacity) {
		if (capacity > data.length) {
			int x = 2 * data.length;

			if (x < capacity)
				x = capacity;

			Object[] arr = new Object[x];

			for (int i = 0; i < size; i++)
				arr[i] = data[i];

			data = arr;
		}
	}

	public int getCapacity() {
		return data.length;
	}

	private int find(Object o, int i, boolean forward) {
		if (i < 0 || i >= size) return -1;

		if (forward) {
			if (o == null) {
				for (; i < size; i++)
					if (data[i] == null)
						return i;
			}
			else {
				for (; i < size; i++)
					if (o.equals(data[i]))
						return i;
			}
		}
		else {
			if (o == null) {
				for (; i >= 0; i--)
					if (data[i] == null)
						return i;
			}
			else {
				for (; i >= 0; i--)
					if (o.equals(data[i]))
						return i;
			}
		}
		return -1;
	}

	public int remove(Object o) {
		int i = find(o, 0, true);
		if (i >= 0)
			remove(i);
		return i;
	}

	public Object remove(int index) {
		if (index < 0 || index >= size)
			throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + size + ")");

		Object o = data[index];

		for (int i = index + 1; i < size; i++)
			data[i-1] = data[i];

		data[--size] = null;
		return o;
	}

	public Object get(int index) {
		if (index < 0 || index >= size)
			throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + size + ")");

		return data[index];
	}

	public Object set(Object o, int index) {
		if (index < 0 || index >= size)
			throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + size + ")");

		Object old = data[index];
		data[index] = o;
		return old;
	}

	public boolean contains(Object o) {
		return find(o, 0, true) >= 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void trimArray() {
		if (size < data.length) {
			Object[] arr = new Object[size];
			for (int i = 0; i < size; i++)
				arr[i] = data[i];
			data = arr;
		}
	}

	public int indexOf(Object o) {
		return find(o, 0, true);
	}

	public int indexOf(Object o, int startIndex) {
		return find(o, startIndex, true);
	}

	public int lastIndexOf(Object o) {
		return find(o, size - 1, false);
	}

	public int lastIndexOf(Object o, int startIndex) {
		return find(o, startIndex, false);
	}
}
