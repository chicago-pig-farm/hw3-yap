//Tree23.java
/*****************************************
 *  	Overview: implementation of 2-3 trees
 *		Tree23 is our main class:
 *			but it depends on two classes:
 *		   		InternalNode class
 *				LeafNode class
 *			which are both extensions of the Node class.
 *		Tree23 instances store Items instances in its leaves.
 *	
 *	MEMBERS: ====================================
 *		Item class:
 *				String key;
 *				int data;
 *		Node class:
 *				Node parent;
 *				String guide;
 *		InternalNode class:
 *				Node[] child[4];	//THIS ALLOWS YOU TO WRITE LOOPS
 *				//If degree is d, then non-null children are child[0..d-1]
 *		LeafNode class:
 *				int data;
 *		Tree23 class:
 *				InternalNode root;
 *				int height;
 *	METHODS: ====================================
 *		Tree23 class:
 *				Item search(String);
 *				boolean insert(Item);
 *				Item delete(String);
 *			All these 3 methods calls the helper method,
 *				InternalNode find(String); // returns a pseudo-leaf
 *			We provide you with some debugging tool:
 *				showTree() -- prints a representation of the tree
 *				dbug(String)
 *				debug(String)
 *	Terminology:
 *		An InternalNode whose children are LeafNodes is
 *			called a "pseudo-leaf"
 *	
 *	====================================
 *	Basic Algorithms, Fall2021
 *	Professor Yap
 *	TA's Bingran Shen and Zihan Feng
 **************************************** */

import java.util.Random;

class Tree23 extends Util {
	// MEMBERS:=========================================
	/////////////////////////////////////////
		InternalNode root = new InternalNode();
		int ht; //height
	// CONSTRUCTORS:====================================
	/////////////////////////////////////////
		Tree23() {// Note: ht=0 iff n=0 (where n is number of items)
			ht = 0; } //  ht=1 implies n=1,2 or 3 
	// METHODS:=========================================
	/////////////////////////////////////////
	InternalNode find (String x){
		// find(x) returns the Internal Node u such that either contains x
		//		or where x would be inserted.  Thus u is a pseudo-leaf.
		//		The node u is NEVER null.
		// 	This is the common helper for search/insert/delete!!

		if(ht == 0) return root;
		return findHelper(root,x);
		}//find


		InternalNode findHelper (InternalNode parent, String key){
			//base case : leaves with keys 
			//if child node is internal node keep going 
			//if child node is leafnode recurse and check the key 
			if(parent.child[0] instanceof LeafNode){
				String tempKey = ((LeafNode)parent.child[0]).item().key;
				if ( tempKey.equals(key)){
					return parent;
				}
			} else if(parent.child[1] instanceof LeafNode){
				String tempKey = ((LeafNode)parent.child[1]).item().key;
				if ( tempKey.equals(key)){
					return parent;
				}
			}
			else if(parent.degree() > 2 && parent.child[2] instanceof LeafNode){
				String tempKey = ((LeafNode)parent.child[2]).item().key;
				if ( tempKey.equals(key)){
					return parent;
				}
			} else {
				for (int i=0; i< parent.degree(); i++){
					InternalNode tempChild = (InternalNode)parent.child[i];
					if (tempChild.guide.compareTo(key) <= 0)  { // key is equal to or less than guide
						return findHelper(tempChild, key); 
					}
				}
				InternalNode maxChild = (InternalNode) parent.child[parent.degree() -1];
				return findHelper(maxChild, key); // in the case key is bigger than all other keys
			}
			return null;
		}

	//TODOTODOTODOTODOTODO
	LeafNode search (String x){
		
		// Returns a null node if failure;
		// 		otherwise, return a LeafNode with the key x.
		InternalNode u = find(x); //check  leaf node 
		for (int i=0; i<u.degree(); i++){
				String tempKey = ((LeafNode)u.child[i]).item().key; //look at all the child via for loop 
				if ( tempKey.equals(x)){
					//might have to cast to leaf node
					return (LeafNode)u.child[i];//if found key in one of the children yay
				}
		}
		return null; // x is not found
		}


	boolean insert (Item it){
		// insert(it) returns true iff insertion is successful.
		if (search(it.key) == null) return false;
		InternalNode u = find(it.key);
		LeafNode child1 = (LeafNode) u.child[1];
		LeafNode child0 = (LeafNode) u.child[0];
		//the node only has 2 children and only needs to add the new node 
		if (u.degree()==2){
			if(child1.item().key.compareTo(it.key) > 0){
				u.child[2] = new LeafNode(it);
			}else if(child0.item().key.compareTo(it.key) > 0){
				u.child[2] = u.child[1];
				u.child[1] = new LeafNode(it);
			}else{
				u.child[2] = u.child[1];
				u.child[1] = u.child[0];
				u.child[0] = new LeafNode(it);
			}
		} else {
			LeafNode child2 = (LeafNode) u.child[2];
		}
		return false; //insertion failed
		}//insert
	Item delete (String x){
		// delete(x) returns the deleted item
		//			returns null if nothing is deleted.
		InternalNode u = find(x);
		return null; // delete fail
		}//delete











	// HELPERS:=========================================
	/////////////////////////////////////////
	// DO NOT CHANGE unitTest
	void unitTest (){
		// unit test for Insert+Search+Delete
		// First input: //////////////////////////////////////
		debug("\n======> Inserting Fruits:");
		String[] fruits =
			{"banana","apple","peach","orange","apple","pear","plum"};
		for (String x: fruits){
			boolean in = insert( new Item(x));
			debug("insert(" + x + ") = " + String.valueOf(in));
		}
		debug("Here is the final Fruit Tree:");
		showTree();

		debug("\n======> Inserting sqrt(3) digits:");
		// Second input: /////////////////////////////////////
		int[] input = {1, 7, 3, 2, 0, 5, 0, 8, 0};
		Tree23 t = new Tree23();
		for (int x : input){
			Item it = new Item(x);
			boolean in = t.insert(it);
			debug("insert(" + x + ") = " + String.valueOf(in));
		}
		debug("Here is the final sqrt(3) Tree:");
		t.showTree();

		// SEARCHES: //////////////////////////////////////
		debug("\n======> Searching Fruits");
		LeafNode v = search("banana");
		if (v==null) 
			debug("Fruit Tree: search(banana) fails");
		else
			debug("Fruit Tree: search(banana) succeeds");
		v = search("cherry");
		if (v==null) 
			debug("Fruit Tree: search(cherry) fails");
		else
			debug("Fruit Tree: search(cherry) succeeds");

		debug("\n======> Searching Digits");
		v = t.search(String.valueOf(3));
		if (v==null) 
			debug("Sqrt3 Tree: search(3) fails");
		else
			debug("Sqrt3 Tree: search(3) succeeds");
		v = t.search(String.valueOf(4));
		if (v==null) 
			debug("Sqrt3 Tree: search(4) fails");
		else
			debug("Sqrt3 Tree: search(4) succeeds");

		// DELETES: //////////////////////////////////////
		debug("\n=============== deleting fruits");
		delete("banana");
			debug("Fruit Tree after DELETE(banana):");
			showTree();
		delete("plum");
			debug("Fruit Tree after DELETE(plum):");
			showTree();
		delete("apricot");
			debug("Fruit Tree after DELETE(apricot):");
			showTree();
		delete("apple");
			debug("Fruit Tree after DELETE(apple):");
			showTree();
		debug("\n=============== deleting digits");
		t.delete(String.valueOf(3));
			debug("Sqrt3 Tree after DELETE(3):");
			t.showTree();
		t.delete(String.valueOf(0));
			debug("Sqrt3 Tree after DELETE(0):");
			t.showTree();
		debug("\n=============== THE END");
		}//unitTest
	void showTree (){
		// print all the keys in 23tree:
		int h=ht;
		InternalNode u=root;
		showTree(u,h); dbug("\n"); }
	void showTree (Node u, int h){
		// internal recursive call for showTree
		if (h==0){
			debug("()"); return;}
		int d=((InternalNode)u).degree();
		dbug("G="+ String.valueOf(u.guide) +":(");
		for (int i=0; i<d; i++)
			if (h==1){
				Node w = ((InternalNode)u).child[i];
				LeafNode v = (LeafNode)w;
				(v.item()).dump();
			} else {
				showTree(((InternalNode)u).child[i],h-1);
			}
		dbug(")"); }//showTree
	boolean multiInsert (Random rg, int n, int m){
		// Insert n times and then search for m.
		// returns true if m is found.
		return false;}//multiInsert
	// MAIN METHOD:=========================
	/////////////////////////////////////////
	public static void main(String[] args){
		int ss = (args.length>0)? Integer.valueOf(args[0]) : 0;
		int nn = (args.length>1)? Integer.valueOf(args[1]) : 10;
		int mm = (args.length>2)? Integer.valueOf(args[2]) : 0;
		
		Random rg = (ss == 0)? new Random() : new Random(ss);
		Tree23 tt = new Tree23();

		switch (mm){
			case 0: //unit test for insert+search
				debug("==> mode 0: unit test\n");
				tt.unitTest();
				// tt.test();
				break;
			case 1: //search for "10" once
				debug("==> mode 1: random insert+search once\n");
				tt.multiInsert(rg, nn, 10);
				break;
			case 2: //search for "10" until succeeds
				debug("==> mode 2: random insert+search till success\n");
				while (!tt.multiInsert(rg, nn, 10))
					debug("\n================ Next Trial\n");
				break;
			case 3: //you may add as many cases as you want
					//for your own testing.
		}
	}//main
}//class Tree23


/*****************************************
HELPER CLASSES: Item and Node
	Item class:
		this is the data of interest to the user
		it determines the remaining details.
	Node class:
		This is the superclass of the two main work horses:
			InternalNode class
		and
			LeafNode class
		The nodes of the Tree23 is made up of these!
	We provide the InternalNode class with useful methods such as
		degree()	-- determine the degree of this node
		addLeaf		-- assumes the children are LeafNodes, and
						we want to add a new leaf.
		removeLeaf	-- converse of addLeaf
		sortNode	-- sort the children of this node
***************************************** */
// Class Item %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Item {
		String key;
		int data;
		Item(String k, int d){
			key = k; data = d; }
		Item(String k){// Randomly generate the int data!!!
			key = k; data = (int)(10*Math.random()); }
		Item(int k){ // Use string value of k as key!!!
			key = String.valueOf(k); data = k; }
		Item(Item I){
			key = I.key; data = I.data; }
		// METHODS:
		void dump(){
			System.out.printf("<%s:%d>", key, data); }
		String stringValue(){
			return String.format("<%s:%d>", key, data); }
	}//class Item
	
// Class Node %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Node extends Util {
		InternalNode parent;
		String guide;
	}//class Node
	
// Class LeafNode %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class LeafNode extends Node {
	int data;
	LeafNode(Item I){
		parent=null; guide=I.key; data=I.data;}
	LeafNode(Item I, InternalNode u){
		parent=u; guide=I.key; data=I.data;}
	//MEMBER:
	//////////////////////////////////////////
	Item item(){
		Item it=new Item(guide, data);
		return it; }
	}//class LeafNode

// Class InternalNode %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class InternalNode extends Node {
	//MEMBERS:
		Node[] child = new Node[4];
	//CONSTRUCTORS:
		InternalNode(){
			this(null,null,null); }
		InternalNode(Node u0, Node u1, InternalNode p){
			this(u0, u1); 
			if (u0!=null) u0.parent=this;
			if (u1!=null) u1.parent=this;}
		//======================================================
		InternalNode(InternalNode u0, InternalNode u1, InternalNode p){
			this((Node)u0, (Node)u1, p); }
		InternalNode(Node u0, Node u1){
			assert(u0==null || u1==null || u0.guide.compareTo(u1.guide)<0);
			if (u1!=null) guide = u1.guide;	
			child[0] = u0; child[1] = u1;
			} // REMEMBER to update u0.parent and u1.parent.
		InternalNode(InternalNode u0, InternalNode u1){
			this((Node)u0, (Node)u1); }
	//METHODS:
	//////////////////////////////////////////
	int degree(){ 
		// get the degree of this InternalNode
		int count = 0;
		for (int i=0; i<child.length; i++){
			if (child[i] != null) count++;
		}
		return count; }//degree
	void newParent (InternalNode u0, InternalNode u1, InternalNode p){
		// newParent(u0, u1, p) sets up p as parent of u0, u1.
		newParent((Node)u0, (Node)u1, p); }
	void newParent (Node u0, Node u1, InternalNode p){
		//assert(u0.key < u1.key)
		p.guide = u1.guide;
		p.child[0] = u0;
		p.child[1] = u1; 
		u0.parent = u1.parent = p; }//newParent
	int addLeaf (Item it){
		// addLeaf(it) returns d:{0,1,2,3,4}
		//		where d=0 means addLeaf failed 
		//			(either it.key is a duplicate or this->degree is 4)
		//		else d is the new degree of this InternalNode.
		//	ALSO: this InternalNode is sorted.
		// assert(this.child[] are leaves)
		return 0; }//addLeaf
	Item removeLeaf (String x){
		// removeLeaf(x) returns the deleted item whose key is x
		//		or it returns null if no such item.
		// assert(this.child[] are leaves)
		return null; }//removeLeaf
	int getIndexOf(InternalNode u){
		//		returns the index c such that this->child[c]==u;
		// assert(u is a child of "this")
		return 0; }//getIndexOf
	void shiftLeft (int c){ // don't forget to delete the last child
		//		this->child[c] is a hole which we must fill up;
		//		so for each i>c: child[i-1] = child[i]
		}//shiftLeft
	void shiftRight (int c){
		// shiftRight(c)
		//		create a hole at child[c], so for each i>c,
		//				child[i] = child[i-1]
		//		(but start with i=degree down to i=c+1)
		// ASSERT("c < this->degree < 4");
		}//shiftRight
	boolean proposeLeft (int c){
		// 			ASSERT("c>0 and child[c].degree=1")
		//		return TRUE if the child[c] merges into child[c-1]
		//		return FALSE child[c] adopts a child of child[c-1].
		//	REMARK:	return TRUE means this is a non-terminal case
		return false;
		}//proposeLeft
	boolean proposeRight (int c){
		// 			ASSERT("c+1<degree and child[c].degree=1")
		//		returns TRUE if the child[c] and child[c+1] are merged.
		//		returns FALSE if child[c] adopts a child of child[c+1].
		// 	REMARKS: under our policy, we KNOW that c==0!
		//		Also TRUE means this is a non-terminal case
		return false;
		}//proposeLeft
	void sortNode(){
		// We use swapNodes to sort the keys in an InternalNode:
		}//SortNode
	void dump (){// print this node
		}//dump
	void search(){

	}
}//class InternalNode
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

