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

public class LinkedList {

	protected Node head = null;
	protected Node tail = null;
	protected int size = 0;

	public LinkedList() {}

	public static class Node {
		protected LinkedList list = null;
		protected Node next = null;
		protected Node prev = null;
		protected Object userObject = null;

		protected Node(LinkedList list, Object userObject) {
			this.list = list;
			this.userObject = userObject;
		}

		public LinkedList list() {
			return list;
		}

		public Node next() {
			return next;
		}

		public Node prev() {
			return prev;
		}

		public Object getUserObject() {
			return userObject;
		}

		public void setUserObject(Object userObject) {
			this.userObject = userObject;
		}

		public void insertBefore(Object o) {
			list.insertBefore(this, o);
		}

		public void insertAfter(Object o) {
			list.insertAfter(this, o);
		}

		public void remove() {
			list.removeNode(this);
		}
	}

	protected Node createNode(Object o) {
		return new Node(this, o);
	}

	protected void insertBefore(Node n, Object o) {
		Node p = createNode(o);

		if (size == 0) {
			head = p;
			tail = p;
		}
		else {
			if (n == head) {
				p.next = head;
				head.prev = p;
				head = p;
			}
			else {
				n.prev.next = p;
				p.prev = n.prev;
				n.prev = p;
				p.next = n;
			}
		}

		size++;
	}

	protected void insertAfter(Node n, Object o) {
		Node p = createNode(o);

		if (size == 0) {
			head = p;
			tail = p;
		}
		else {
			if (n == tail) {
				p.prev = tail;
				tail.next = p;
				tail = p;
			}
			else {
				n.next.prev = p;
				p.next = n.next;
				n.next = p;
				p.prev = n;
			}
		}

		size++;
	}

	protected Object removeNode(Node n) {
		if (size == 0)
			return null;

		Object o = n.userObject;

		if (n == head) {
			head = head.next;

			if (head == null)
				tail = null;
			else
				head.prev = null;
		}
		else if (n == tail) {
			tail = tail.prev;
			tail.next = null;
		}
		else {
			n.prev.next = n.next;
			n.next.prev = n.prev;
		}

		n.list = null;
		size--;
		return o;
	}

	public Node getHead() {
		return head;
	}

	public Node getTail() {
		return tail;
	}

	public void addToHead(Object o) {
		insertBefore(head, o);
	}

	public void addToTail(Object o) {
		insertAfter(tail, o);
	}

	public Object removeHead() {
		return removeNode(head);
	}

	public Object removeTail() {
		return removeNode(tail);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(6 * size);
		sb.append("[");
		Node n = head;

		if (n != null) {
			sb.append(n.userObject);
			n = n.next;
		}

		while (n != null) {
			sb.append(", ");
			sb.append(n.userObject);
			n = n.next;
		}

		return sb.append("]").toString();
	}
}
