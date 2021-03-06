package com.hashtable.closedaddressing;

import java.util.LinkedList;

/* HashTable(HashMap) data structure implementation...
using the following Closed-Addressing collision resolution technique: 
- Separate chaining using Linked Lists
*/
public class HashTable {

	private LinkedList<Entry>[] hashTable; // Hashtable array of LinkedList of Entry( a string key,value pair)
	private int size; // number of  hashtable buckets/slots/array elements.
	private int itemCount; // number of items currently in the hashtable
	private double loadFactor; // load factor of 3.0
	
	public HashTable(int initialSize) {
		this.size = initialSize;
		this.hashTable = new LinkedList[initialSize];
		this.itemCount = 0;
		this.loadFactor = 3.0;
	}
	
	
	// Nested inner class for creating Entries (key,value pairs) for the hash table.
	// Our entries will pair a person's Name with a car name
	public static class Entry {
		String key;
		String value;
		
		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	
	
	// ******** Hash Table(Hash Map Methods)**************
	
	// hash function method  - returns the index of the given key based on it's
	// hashCode that is passed in the function to calculate the index.
	public int hashFunction(String key) {
		int hashCode = key.hashCode();
		return Math.abs(hashCode) % hashTable.length;
	}
	
	// insert method - adds entry into hash table or if key already exists, 
	// update its value
	public void put(String key, String value) {
		
		// if null key is being inserted
		if(key == null) {
			System.out.println("Cannot insert entry: (' NULL , " + value + "')... Nulls not allowed in HashTable!");
			return;
		}

		int index = hashFunction(key);
		
		Entry newEntry = new Entry(key, value);
		
		// if the bucket at index already has data (aka a linkedlist of entry nodes)
		if(hashTable[index] != null) {
			
			// loop through the linkedlist at this table index
			// if passed key matches existing key of an Entry in the list,
			// then we just update it's value with the new value.
			for(Entry entry : hashTable[index]) {
				if(entry.getKey().equals(key)){
					entry.setValue(value);
					System.out.println("Updated entry: ('" + key + ", " + value + "') @ index: " + index);
					return;
				}
			}
			
			// Otherwise, we have a collision
			System.out.println("Collision! @index: " + index + " for Entry('" + key + "', '"+ value + 
					"')! Adding to end of this index....");
			System.out.println();
			
			hashTable[index].add(newEntry);
			itemCount++;
		}
		
		// otherwise, if the bucket at index is null, then insert new Entry
		if(hashTable[index] == null) {
			
			hashTable[index] = new LinkedList<>();
			hashTable[index].add(newEntry);
			System.out.println("Insert entry: ('" + key + ", " + value + "') @ index: " + index);
			itemCount++;
			
			// auto resize if table reaches or passes load factor of 3.0
			if(itemCount/hashTable.length >= loadFactor)
				resize();
			
			return;
		}

	}
	
	
	// Access method - returns the value of the given key if it exists in the table
	public String get(String key) {
		
		int index = hashFunction(key);
		
		if(key == null)
			return null;
		
		// loops through the list of entries at the index of the hashtable
		// if the entry's key matches given key, then return entry's value
		while(hashTable[index] != null) {
			for(Entry entry : hashTable[index]) {
				if(entry.getKey().equals(key))
					return entry.getValue();	
			}
			return null;
		}
		return null;
	}
	
	
	// Delete method - remove(key) - returns the Entry of given key
	// and set's the element of the index to null again.
	public Entry remove(String key) {
		
		Entry toDelete = null;
		
		if(key == null)
			return toDelete;
		
		int index = hashFunction(key);
		
		if(!isEmpty()) {
			if(hashTable[index] != null) {
				for(Entry entry: hashTable[index]) {
					if(entry.getKey().equals(key)) {
						toDelete = entry;
						hashTable[index].remove(entry);
						itemCount--;
						return toDelete;
					}
				}
			}
		}
			
		return toDelete;
	}
	
	
	// Search method - contains(key) - 
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	
	
	// ********** Helper Methods ***************************
	
	// empty method - returns true if table is empty
	public boolean isEmpty() {
		return itemCount == 0;
	}
	
	// full method - returns true if table is full
	public boolean isFull() {
		return itemCount == hashTable.length;
	}
	
	
	// print method-   displays the hash table visually.
	public void print() {
		System.out.println();
		System.out.println("HashTable is: ");
		System.out.println("INDEX | ENTRY ");
		for(int i = 0; i < hashTable.length; i++) {
			if(hashTable[i] != null) {
				System.out.printf("%02d | ", i);
				for(Entry entry: hashTable[i])
					System.out.printf("('%s', ' %s'), ", entry.getKey(), entry.getValue());
				System.out.println();
			}
			else {
				System.out.printf("%02d | nil\n", i);
			}
		}
		System.out.println();
		System.out.println();
	}
	
	// size method - returns the number of Entries in the hashtable
	public int size() {
		return itemCount;
	}
	
	// resize method - increases the capacity of the Hash Table
	public void resize() {
		
		System.out.println();
		System.out.println("Resizing...........");
		System.out.println();
		
		int newCapacity = hashTable.length*2;
		LinkedList<Entry>[] tempArr = hashTable;
		hashTable = new LinkedList[newCapacity];
		itemCount = 0;
		
		for(LinkedList<Entry> list: tempArr) {
			for(Entry entry: list) {
				if(entry != null)
					put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	
	
	
	// execution
	public static void main(String[] args) {
		
		HashTable ht = new HashTable(7);
		
		System.out.println("********Initial HashTable********");
		ht.print();
		
		System.out.println("********Inserting Entries********\n");
		
		ht.put("Kevin", "Software Engineer");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Glen", "Gardener");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Thomas", "Cashier");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Scully", "Doctor");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Fox", "FBI agent");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Alexandra", "Lawyer");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Lincoln", "Lawyer");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		ht.put("Gandolf", "Wizard");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		
		// insert duplicate key - table should update with new value
		ht.put("Thomas", "Cashier's Manager");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		// try inserting a null
		// insert duplicate key - table should update with new value
		ht.put(null, "unemployed");
		ht.print();
		System.out.println("Current size: " + ht.size());
		
		System.out.println("\n********Accessing Entries********\n");
		
		// this one exists in the table
		System.out.println("Get the job(value) of 'Gandolf': " + ht.get("Gandolf"));
		
		// try to access a name that isn't in the table... "ACCESS DENIED 007!"
		System.out.println("Get the job(value) of 'Bond': " + ht.get("Bond"));
		
	
		System.out.println("\n********Searching Entries********\n");
		
		// this doesn't exist in the table, James Bond is a mystery!
		System.out.println("Does 'Bond' exist in table? " + ht.contains("Bond"));
		
		
		
		// this one does exist in the table
		System.out.println("Does 'Glen' exist in table? " + ht.contains("Glen"));
		
		System.out.println("\n********Deleting Entries********\n");
		
		System.out.println("Current size: " + ht.size());
		System.out.println("Removed entry's value of 'Thomas' from the table: " + ht.remove("Thomas").getValue());
		ht.print();
		System.out.println("Current size: " + ht.size());

	}

}


/*Output:
********Initial HashTable********

HashTable is: 
INDEX | ENTRY 
00 | nil
01 | nil
02 | nil
03 | nil
04 | nil
05 | nil
06 | nil


********Inserting Entries********

Insert entry: ('Kevin, Software Engineer') @ index: 6

HashTable is: 
INDEX | ENTRY 
00 | nil
01 | nil
02 | nil
03 | nil
04 | nil
05 | nil
06 | ('Kevin', ' Software Engineer'), 


Current size: 1
Insert entry: ('Glen, Gardener') @ index: 5

HashTable is: 
INDEX | ENTRY 
00 | nil
01 | nil
02 | nil
03 | nil
04 | nil
05 | ('Glen', ' Gardener'), 
06 | ('Kevin', ' Software Engineer'), 


Current size: 2
Collision! @index: 6 for Entry('Thomas', 'Cashier')! Adding to end of this index....


HashTable is: 
INDEX | ENTRY 
00 | nil
01 | nil
02 | nil
03 | nil
04 | nil
05 | ('Glen', ' Gardener'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 3
Insert entry: ('Scully, Doctor') @ index: 0

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), 
01 | nil
02 | nil
03 | nil
04 | nil
05 | ('Glen', ' Gardener'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 4
Collision! @index: 5 for Entry('Fox', 'FBI agent')! Adding to end of this index....


HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), 
01 | nil
02 | nil
03 | nil
04 | nil
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 5
Insert entry: ('Alexandra, Lawyer') @ index: 4

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), 
01 | nil
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 6
Insert entry: ('Lincoln, Lawyer') @ index: 1

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), 
01 | ('Lincoln', ' Lawyer'), 
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 7
Collision! @index: 0 for Entry('Gandolf', 'Wizard')! Adding to end of this index....


HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), ('Gandolf', ' Wizard'), 
01 | ('Lincoln', ' Lawyer'), 
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier'), 


Current size: 8
Updated entry: ('Thomas, Cashier's Manager') @ index: 6

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), ('Gandolf', ' Wizard'), 
01 | ('Lincoln', ' Lawyer'), 
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier's Manager'), 


Current size: 8
Cannot insert entry: (' NULL , unemployed')... Nulls not allowed in HashTable!

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), ('Gandolf', ' Wizard'), 
01 | ('Lincoln', ' Lawyer'), 
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), ('Thomas', ' Cashier's Manager'), 


Current size: 8

********Accessing Entries********

Get the job(value) of 'Gandolf': Wizard
Get the job(value) of 'Bond': null

********Searching Entries********

Does 'Bond' exist in table? false
Does 'Glen' exist in table? true

********Deleting Entries********

Current size: 8
Removed entry's value of 'Thomas' from the table: Cashier's Manager

HashTable is: 
INDEX | ENTRY 
00 | ('Scully', ' Doctor'), ('Gandolf', ' Wizard'), 
01 | ('Lincoln', ' Lawyer'), 
02 | nil
03 | nil
04 | ('Alexandra', ' Lawyer'), 
05 | ('Glen', ' Gardener'), ('Fox', ' FBI agent'), 
06 | ('Kevin', ' Software Engineer'), 


Current size: 7

*/