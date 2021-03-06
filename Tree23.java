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
			if (parent.child[0] instanceof InternalNode){
				for (int i=0; i < parent.degree(); i++){
					System.out.println("parent: " + parent.guide);
					InternalNode tempChild = (InternalNode)parent.child[i];
					System.out.println("tempChild: " + tempChild.guide);
					if(tempChild.guide.equals(key)){ // the key exists in the subtree, return the index 
						System.out.println("found tempChild " + tempChild.guide + " the parent of found tempChild is " + parent.guide);
						return tempChild;
					}else { //didn't find in subtree, recurse
						return findHelper(tempChild, key); //recurse down most likely branch
					}
				}
				InternalNode maxChild = (InternalNode) parent.child[parent.degree() -1];
				return findHelper(maxChild, key); // in the case key is bigger than all other keys
			}
			return parent; //at leaf level but didn't find exact key so item belongs here
		}//findHelper

	//TODOTODOTODOTODOTODO
	LeafNode search (String x){	
		// Returns a null node if failure;
		// otherwise, return a LeafNode with the key x.
		System.out.println("searching for " + x);
		InternalNode u = find(x); 
		System.out.println(u.guide);
		for (int i=0; i<u.degree(); i++){
			String tempKey = ((LeafNode)u.child[i]).item().key; //look at all the child via for loop 
			//System.out.println(u.child[i].guide);
			if ( tempKey.equals(x)){
				//might have to cast to leaf node
				return (LeafNode)u.child[i];//if found key in one of the children yay
			}
			else{
				
			}
		}
		return null; // x is not found
	}

	void justGuides(InternalNode n){
		if (n == null || n.parent == null) return;
		if(n.greaterThan(n.parent.guide)){
			n.parent.guide = n.guide;
			justGuides(n.parent);
		}
	}

	
	void parentGuides(InternalNode p, InternalNode c){
		//base case
		if(p == null ){ //adding new parent node at root level
			ht++;
			InternalNode nextRoot = new InternalNode();
			if(root.guide.compareTo(c.guide) >0){ //root guide is bigger than new parent guide

				nextRoot.guide = root.guide;
				nextRoot.child[0] =c;
				nextRoot.child[1] =root;
			} else {
				nextRoot.guide = c.guide;
				nextRoot.child[0] = root;
				nextRoot.child[1] = c;
			}
			root.parent = nextRoot;
			c.parent = nextRoot;
			root = nextRoot;
			return; //exits
		} 
		else if(p.degree()<=2){
			p.child[2] = c;
			c.parent = p;
			p.sortNode();
			justGuides(p);
		}else{
			//parent already has 3 children
			if(p.parent !=null){
				//see if this node can be shifted to a sibling (grandparents other children)
				InternalNode gparent = p.parent;
				if(((InternalNode)gparent.child[0]).degree() == 2 && 
					gparent.child[2] != null && ((InternalNode)gparent.child[2]).degree() == 2 &&
					((InternalNode)gparent.child[0]) != p &&
					((InternalNode)gparent.child[2]) != p){
					//left and right siblings have space and I'm not child 0 or 2
					parentGuides((InternalNode)gparent.child[2], c);
					parentGuides((InternalNode)gparent.child[0], (InternalNode)p.child[0]);
					p.child[0] = p.child[1];
					p.child[1] = p.child[2];
					p.child[2] = null;
					p.guide = p.child[1].guide;
					return;
				}

				if(((InternalNode)gparent.child[0]).degree() == 2){
					parentGuides((InternalNode)gparent.child[0], c);
					return;
				}else if (((InternalNode)gparent.child[1]).degree() == 2) {
					parentGuides((InternalNode)gparent.child[1], c);
					return;
				}else if (gparent.child[2] != null && ((InternalNode)gparent.child[2]).degree() == 2) {
					parentGuides((InternalNode)gparent.child[2], c);
					return;
				}
			}
			//need to split internal node to make another parent
			InternalNode p2 = new InternalNode();
			if( c.greaterThan(p.child[2].guide)){ //c is the largest
				p2.guide = c.guide;
				p2.child[1] = c;
				p2.child[0] = p.child[2];
				p.child[2] = null;
				p.guide = p.child[1].guide;
			}else if( c.greaterThan(p.child[1].guide)){ 
				p2.child[0] = c;
				p2.child[1] = p.child[2];
				p2.guide = p2.child[0].guide;
				p.child[2] = null; //remove childe from previous parent
				p.guide = p.child[1].guide;
			}else if( c.greaterThan(p.child[0].guide)){ 
				p2.child[0] = p.child[1]; // two largest going to belong to IN p2
				p2.child[1] = p.child[2];
				p2.guide = p2.child[0].guide;
				p.child[2] = null; //remove child from previous parent
				p.child[1] = c; //two smallest staying with IN p
				p.guide = c.guide; //updating guides
			}else { // c is smallest of all the internal nodes of p
				p2.child[0] = p.child[2];
				p2.child[1] = p.child[1];
				p2.guide = p2.child[1].guide;
				p.child[2] = null;
				p.child[1] = p.child[0];
				p.child[0] = c;
				p.guide = p.child[1].guide;
			}
			parentGuides(p.parent, p2); //make sure p2 is in the tree
		}
	}


	boolean insert (Item it){
		// insert(it) returns true iff insertion is successful.
		if (search(it.key) != null) return false;
		InternalNode u = find(it.key);
		int leafNumber = u.addLeaf(it);
		justGuides(u);
		if(ht == 0 ){ ht++;}
		else if (u.degree() > 3) {
			InternalNode nextP = new InternalNode();
			nextP.updateGuide(u.child[2], u.child[3], nextP);
			u.updateGuide(u.child[0], u.child[1], u);
			parentGuides(u.parent, nextP);
		}
		return true;
	}//insert


	Item delete (String x){
		// delete(x) returns the deleted item
		//			returns null if nothing is deleted.
		if (search(x) != null) return null; //the item trying to delete isn't in the tree
		LeafNode l = search(x); //find the leafnode being deleted
		
		InternalNode p = l.parent;
		int pIndex = p.getIndexOf(p);
		Item it = p.removeLeaf(x);
		if(it.key == x){
			
			for(int i=0; i<p.degree(); i++){
				if(p.child[pIndex-1] != null){
					p.proposeLeft(pIndex); //parent has a left sibling	
					return it;
				} else{ //if it doesnt have a left sibling it must have a right sibling
					p.proposeRight(pIndex);
					return it;
				}
			}
		}	
		return null;
		
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

	void showTree () {
            // print all the keys in 23tree:
            int h = ht;
            InternalNode u = root;
            showTree(u, h, "");
            dbug("\n");
        }// showTree


    void showTree (Node u, int h, String offset) {
	    // internal recursive call for showTree
	    if (h == 0) {
	        debug("()");
	        return;
	    }
	    ////System.out.println("Height = "+h);
	    if(u instanceof InternalNode ){
	    int d = ((InternalNode) u).degree();
	    String increment = "G=" + String.valueOf(u.guide) + ":(";
	    // Note: "G=" refers to the "guide"
	    dbug(increment);
	    offset = offset + tab(increment.length() - 1, '-') + "|";
	    for (int i = 0; i < d; i++)
	        if (h == 1) {
	            Node w = ((InternalNode) u).child[i];
	            LeafNode v = (LeafNode) w;
	            (v.item()).dump();
	            if (i == d - 1)
	                debug(")");
	        } else {
	            if (i > 0)
	                dbug(offset);
	            showTree(((InternalNode) u).child[i], h - 1, offset);
	        }
	    }
	     //dbug(")");
	    else {
	    	System.out.println("leaf: " + u.guide);
	    }
	}// showTree

    /*String tab (int n, char c){
        // returns a string of length n filled with c
        char[] chars = new char[n];
        java.util.Arrays.fill(chars, c);
        return String.valueOf(chars);
    }//tab
	*/
    Tree23 randomTree (Random rg, int n, int N) {
        // Insert n times into empty tree, and return the tree.
        // Use rg as random number generator keys in range [0,N)
        // Keep the size of tree in Util.COUNT
        Tree23 t = new Tree23();
        for (int i = 0; i < n; i++) {
            int x = rg.nextInt(N);
            Item it = new Item(x);
            boolean b = t.insert(it);
            if (b)  COUNT++;
        } // for
        return t;
    }// randomTree

    int randomDelete (Random rg, Tree23 tt, int N) {
        // delete a random element in the tree 100 times until it's empty
        // Use rg as random number generator keys in range [0,N)
        // return the delete count if tree is empty
        int count = 0;
        Item it;
        while (count < 100 && tt.ht > 0) {
            it = tt.delete(String.valueOf(rg.nextInt(N)));
            count++;
        }
        return count;
    }// randomDelete


    void messUpTree (String key) {
        InternalNode u = find(key);
        int deg = u.degree();
        u.swapNodes(0, deg - 1);
    }// messUpTree


    String checkTree () {          
        // returns error message iff the keys are in NOT in sorted order!
        // CAREFUL: we do not check the guides
        int h = ht;
        InternalNode u = root;
        String s = checkTree(u, h, ""); // "" is the globally least key!
        // if (s==null) // May NOT be error! let the caller decide.
        // return "CHECKTREE ERROR";
        return s;
    }


    String checkTree (Node u, int h, String maxkey) {
        // internal recursive call for checkTree
        // returns null if fail; else it is the maximum seen so far!
        if (h == 0)
            return "OK, EMPTY TREE";
        int d = ((InternalNode) u).degree();
        for (int i = 0; i < d; i++)
            if (h == 1) {
                Node w = ((InternalNode) u).child[i];
                LeafNode v = (LeafNode) w;
                if (maxkey.compareTo(v.item().key) >= 0) { // error!
                    debug("CHECKTREE ERROR at leaf " + v.item().key);
                    debug(maxkey);
                    return null;
                } else
                    maxkey = v.item().key;
            } else {
                String s1 = checkTree(((InternalNode) u).child[i], h - 1, maxkey);
                if (s1 == null || maxkey.compareTo(s1) >= 0) {
                    return null;
                } else
                    maxkey = s1;
            }
        return maxkey;
    }// checkTree


	boolean multiInsert (Random rg, int n, int m){
		// Insert n times and then search for m.
		// returns true if m is found.
		return false;}//multiInsert


	// MAIN METHOD:=========================
	/////////////////////////////////////////
	
    public static void main (String[] args) {
        int ss = (args.length > 0) ? Integer.valueOf(args[0]) : 0;
        int nn = (args.length > 1) ? Integer.valueOf(args[1]) : 10;
        int mm = (args.length > 2) ? Integer.valueOf(args[2]) : 0;
   
        Random rg = (ss == 0) ? new Random() : new Random(ss);
        Tree23 tt = new Tree23();
   
        switch (mm) {
            case 0: // unit test for insert+search
                debug("==> mode 0: unit test\n");
                tt.unitTest();
                // tt.test();
                break;
            case 1: // search for "10" once
                debug("==> mode 1: random insert+search once\n");
                tt.multiInsert(rg, nn, 10);
                break;
            case 2: // search for "10" until succeeds
                debug("==> mode 2: random insert+search till success\n");
                while (!tt.multiInsert(rg, nn, 10))
                    debug("\n================ Next Trial\n");
                break;
            case 3: // you may add as many cases as you want
                    // for your own testing.
           
            case 101: // create a random tree with nn random insertions
                debug("==> mode 101: create a random tree\n");
                tt = tt.randomTree(rg, nn, 2 * nn);
                debug("Randomly generated tree of is:");
                tt.showTree();
                break;
            case 102: // create a random tree and randomly delete 100 times until it's empty
                debug("==> mode 102: create a random tree and delete till tree is empty\n");
                tt = tt.randomTree(rg, nn, 2 * nn);
                debug("Randomly generated tree of is:");
                tt.showTree();
                int count = tt.randomDelete(rg, tt, 2 * nn);
                if (tt.ht >= 1) {
                    debug("Tree non-empty after 100 random deletes, here is what's left:");
                    tt.showTree();
                } else
                    debug("After " + String.valueOf(count) + " deletes, tree is empty");
                break;
        }
    }// main
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

		//This leaf node is greater than the item's value
	boolean greaterThan(Item it){

		if( guide.compareTo(it.key) > 0 ){
			return true;
		}
		else return false;
	}

		//This leaf node is greater than the item's value
	boolean greaterThan(String s){
		if(guide.compareTo(s) > 0 ){
			return true;
		}else return false;
	}

	//This leaf node is less than the item's value
	boolean lessThan(Item it){
		if(guide.compareTo(it.key) < 0 ){
			return true;
		}else return false;
	}

	//This leaf node is less than the item's value
	boolean lessThan(String s){
		if(guide.compareTo(s) < 0 ){return true;
		}
		else return false;
	}
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
			this((Node)u0, (Node)u1); 
	}//class Internal Node



	//HELPER METHODS: 
	//////////////////////////////////////////
	int degree(){ 
		// get the degree of this InternalNode
		int count = 0;
		for (int i=0; i<this.child.length; i++){
			if (child[i] != null) count++;
		}
		return count; 
	}//degree


	void updateGuide (Node u0, Node u1, InternalNode p){
		//assert(u0.key < u1.key);
		p.guide = u1.guide;
		p.child[0] = u0;
		p.child[1] = u1; 
		u0.parent = p;
		u1.parent = p; 
		p.child[2] = null;
		p.child[3] = null;
	}//updateGuide


	int addLeaf (Item it){
		// addLeaf(it) returns d:{0,1,2,3,4}
		//(either it.key is a duplicate or this->degree is 4) 
		//where d=0 means addLeaf failed 
		if( this.degree() == 4) return 0; 
		else{
			//else d is the new degree of this InternalNode.
			//ALSO: this InternalNode is sorted.
			//assert(this.child[] are leaves)
			int i;
			for( i = 0; i<this.degree(); i++){
				assert(this.child[i] instanceof LeafNode);
				if( this.child[i].greaterThan(it)){
					shiftRight(i);
					child[i] = new LeafNode(it);
					child[i].parent = this;
					return i;
				}
			}
			child[i] = new LeafNode(it);
			child[i].parent = this;
			this.guide = child[i].guide;
			return i; //increase the degree by one and return 
		}
	}//addLeaf


	Item removeLeaf (String x){
		// removeLeaf(x) returns the deleted item whose key is x
		//		or it returns null if no such item.
		// assert(this.child[] are leaves)
		for(int i=0; i<this.degree(); i++){
			assert (this.child[i] instanceof LeafNode);
			Item temp = ((LeafNode)this.child[i]).item();
			if(temp.key.equals(x)){
				shiftLeft(i);
				return temp;
			}
		}
		return null; 
	}//removeLeaf


	int getIndexOf(InternalNode u){
		//		returns the index c such that this->child[c]==u;
		// assert(u is a child of "this")
		assert (u.parent == this);
		for(int i=0; i<3; i++){
			if (this.child[i] != null){
				if (this.child[i] == u)
					return i;
			}
		}return -1;	
	}//getIndexOf


	void shiftLeft (int c){ // don't forget to delete the last child
		//		this->child[c] is a hole which we must fill up;
		//		so for each i>c: child[i-1] = child[i]
		for(int i=this.degree(); i>c; i--){
			this.child[i-1] = this.child[i];
			(this.child[4])=null;
		}
	}//shiftLeft


	void shiftRight (int c){
		// shiftRight(c)
		//		create a hole at child[c], so for each i>c,
		//				child[i] = child[i-1]
		//		(but start with i=degree down to i=c+1)
		// ASSERT("c < this->degree < 4");
		assert (c < this.degree() && this.degree() <4);
		//this.child[c] = new LeafNode();
		for (int i=this.degree(); i>c; i--){
			this.child[i] = this.child[i-1];
		}
	}//shiftRight


	boolean proposeLeft (int c){
		// 			ASSERT("c>0 and child[c].degree=1")
		//		return TRUE if the child[c] merges into child[c-1]
		//		return FALSE child[c] adopts a child of child[c-1].
		//	REMARK:	return TRUE means this is a non-terminal case
		InternalNode n = (InternalNode) this.child[c];
		InternalNode nLeft = (InternalNode) this.child[c-1];
		assert (c>0 && (n.degree()== 1));
		
		if(nLeft.degree()==2){  //merge if the left sibling is of degree 2
			//make the parent of child[c-1] new parent of child[c]
			//"delete" the parent node of child[c]

			//the parent of child[c-1] adds a 3rd child which is child[c]
			nLeft.child[2] = n.child[0];
			n = null;
 			return true;
		}else if(nLeft.degree()==3){  //child[c] adopt from child[c-1]
			//child2 from child[c-1] becomes child1 of child[c]
			n.child[1] = nLeft.child[2];
			return false;
		}
		return false;
	}//proposeLeft


	boolean proposeRight (int c){
		// 			ASSERT("c+1<degree and child[c].degree=1")
		//		returns TRUE if the child[c] and child[c+1] are merged.
		//		returns FALSE if child[c] adopts a child of child[c+1].
		// 	REMARKS: under our policy, we KNOW that c==0!
		//		Also TRUE means this is a non-terminal case
		InternalNode n = (InternalNode) this.child[c];
		InternalNode nRight = (InternalNode) this.child[c+1];

		assert (c+1<n.degree() && n.degree()==1);
		
		if(nRight.degree()==2){  //merge if right sibling has 2 children
			//make the parent of child[c+1] new parent of child[c]
			//"delete" the parent node of child[c]
			
			//the parent of child[c-1] adds a 3rd child which is child[c]
			n.child[2] = child[c];
			child[c] = null;
			return true;
		}else if(nRight.degree()==3){  //child[c] adopt from child[c+1]
			//child2 from child[c-1] becomes child1 of child[c]
			n.child[1] = nRight.child[2];
			return false;
		
		}
		return false;
	}//proposeLeft


	void swapNodes(int u0, int u1) {// (u0,u1) <- (u1,u0)
            Node tmp = child[u1];
            child[u1] = child[u0];
            child[u0] = tmp;
        }
    void swapNodes(int u0, int u1, int u2) {// (u0,u1,u2) <- (u2,u0,u1)
        Node tmp = child[u2];
        child[u2] = child[u1];
        child[u1] = child[u0];
        child[u0] = tmp;
    }
    void swapNodes(int u0, int u1, int u2, int u3) {
        // (u0,u1,u2,u3) <- (u3,u0,u1,u2)
        Node tmp = child[u3];
        child[u3] = child[u2];
        child[u2] = child[u1];
        child[u1] = child[u0];
        child[u0] = tmp;
    }
    void sortNode() {
        // We use swapNodes to sort the keys in an InternalNode:
        if (child[1] == null)
            return;
        if (child[0].guide.compareTo(child[1].guide) > 0)
            swapNodes(0, 1);
        // assert(child0 < child1)
        if (child[2] == null)
            return;
        if (child[0].guide.compareTo(child[2].guide) > 0)
            swapNodes(0, 1, 2);
        else if (child[1].guide.compareTo(child[2].guide) > 0)
            swapNodes(1, 2);
        // assert(child0 < child1 < child2)
        if (child[3] == null)
            return;
        if (child[0].guide.compareTo(child[3].guide) > 0)
            swapNodes(0, 1, 2, 3);
        else if (child[1].guide.compareTo(child[3].guide) > 0)
            swapNodes(1, 2, 3);
        else if (child[2].guide.compareTo(child[3].guide) > 0)
            swapNodes(2, 3);
        guide = child[degree()-1].guide;
    }// SortNode


	void dump(){// print this node
		System.out.println("Internal Node");
		for(int i=0; i<degree(); i++){
			System.out.println("children within IN " + child[i].guide);
		}
		}//dump
	void search(){

	}
}//class InternalNode
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
