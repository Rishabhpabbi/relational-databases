package a2;

import a2.erd.*;
import a2.sql.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ImplementMeTest {

    @Test
    @DisplayName("Single entity set with a single attribute which is the PK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case01() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of());
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "A", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"))),
                        new Key(new AttributeSet(List.of(new Attribute("a1")))), // primary key
                        new ConnectionList(), // connections = empty
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Table firstTable = new Table();
        firstTable.name = "A";
        firstTable.attributes = new AttributeSet(List.of(
                new Attribute("a1")
        ));
        firstTable.primaryKey = new Key(new AttributeSet(List.of(
                new Attribute("a1")
        )));
        firstTable.foreignKeys = new ForeignKeySet(); // empty fk set

        Database db = new Database();
        db.add(firstTable);

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("Single entity set with two attributes, which are both part of the PK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case02() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of());
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "A", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2")))), // primary key
                        new ConnectionList(), // connections = empty
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Table firstTable = new Table();
        firstTable.name = "A";
        firstTable.attributes = new AttributeSet(List.of(
                new Attribute("a1"),
                new Attribute("a2")
        ));
        firstTable.primaryKey = new Key(new AttributeSet(List.of(
                new Attribute("a1"),
                new Attribute("a2")
        )));
        firstTable.foreignKeys = new ForeignKeySet(); // empty fk set

        Database db = new Database();
        db.add(firstTable);

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("Single entity set with two attributes, in which only one is part of the PK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case03() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of());
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "A", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1")))), // primary key
                        new ConnectionList(), // connections = empty
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Table firstTable = new Table();
        firstTable.name = "A";
        firstTable.attributes = new AttributeSet(List.of(
                new Attribute("a1"),
                new Attribute("a2")
        ));
        firstTable.primaryKey = new Key(new AttributeSet(List.of(
                new Attribute("a1")
        )));
        firstTable.foreignKeys = new ForeignKeySet(); // empty fk set

        Database db = new Database();
        db.add(firstTable);

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship with no attributes and single-attribute keys")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case04() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "ShopsAt",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Customer", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Store", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Customer",
                            new AttributeSet(List.of(
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Store",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "ShopsAt",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("store_id"))),
                                        db.get(1),
                                        new AttributeSet(List.of(new Attribute("store_id")))
                                ),
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("customer_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("customer_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship with one PK attribute and single-attribute keys")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case05() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "ShopsAt",
                        new AttributeSet(List.of( // attributes
                                new Attribute("date"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("date")))) // primary key
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Customer", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Store", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Customer",
                            new AttributeSet(List.of(
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Store",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "ShopsAt",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("date"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("date")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("store_id"))),
                                        db.get(1),
                                        new AttributeSet(List.of(new Attribute("store_id")))
                                ),
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("customer_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("customer_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship with one PK and one non-PK attribute and single-attribute keys")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case06() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "ShopsAt",
                        new AttributeSet(List.of( // attributes
                                new Attribute("date"),
                                new Attribute("purchase_amount"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("date")))) // primary key
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Customer", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Store", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Customer",
                            new AttributeSet(List.of(
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Store",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "ShopsAt",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("purchase_amount"),
                                new Attribute("date"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("date")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("store_id"))),
                                        db.get(1),
                                        new AttributeSet(List.of(new Attribute("store_id")))
                                ),
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("customer_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("customer_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A one-many or many-one relationship with no attributes and single-attribute keys")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case07() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "Prefers",
                        new AttributeSet(),
                        new Key(new AttributeSet())
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Customer", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Store", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Store",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Customer",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("store_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("store_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A one-many or many-one relationship with one PK attribute and single-attribute keys")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case08() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "Prefers",
                        new AttributeSet(List.of( // attributes
                                new Attribute("since"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("since"))))
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Customer", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Store", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Customer",
                            new AttributeSet(List.of(
                                new Attribute("customer_id"),
                                new Attribute("customer_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("customer_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Store",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("store_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Prefers",
                            new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("customer_id"),
                                new Attribute("since"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("store_id"),
                                new Attribute("since")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("store_id"))),
                                        db.get(1),
                                        new AttributeSet(List.of(new Attribute("store_id")))
                                ),
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("customer_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("customer_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("Two entity sets, one of which is designated as a parent of the other")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case09() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "ManagerIsAnEmployee",
                        new AttributeSet(),
                        new Key()
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Employee", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("employee_id"),
                                new Attribute("employee_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("employee_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Manager", // name
                        new AttributeSet(),
                        new Key(), // primary key
                        new ConnectionList(), // connections
                        new ParentList(List.of( input_erd.relationships.get(0) )), // parents
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Employee",
                            new AttributeSet(List.of(
                                new Attribute("employee_id"),
                                new Attribute("employee_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("employee_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Manager",
                            new AttributeSet(List.of(
                                new Attribute("employee_id"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("employee_id")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("employee_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("employee_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A basic weak entity set")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case10() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "FoundIn",
                        new AttributeSet(),
                        new Key()
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Building", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("building_id"),
                                new Attribute("building_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("building_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Room", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("room_number"),
                                new Attribute("max_capacity"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("room_number")))), // primary key
                        new ConnectionList(), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList(List.of( input_erd.relationships.get(0) ))
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Building",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("building_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("building_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Room",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number"),
                                new Attribute("max_capacity"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("building_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("building_id")))
                                )))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship using a 2-attribute FK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case11() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "R",
                        new AttributeSet(),
                        new Key()
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "A", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "B", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("b1"),
                                new Attribute("b2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "A",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "B",
                            new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "R",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("b1"),
                                new Attribute("b2")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(new Attribute("a1"))),
                                        db.get(0),
                                        new AttributeSet(List.of(new Attribute("a1")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("b1"),
                                                new Attribute("b2"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("b1"),
                                                new Attribute("b2"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship using two 2-attribute FK's")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case12() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "R",
                        new AttributeSet(),
                        new Key()
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "A", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "B", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("b1"),
                                new Attribute("b2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "A",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "B",
                            new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "R",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"),
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"),
                                new Attribute("b1"),
                                new Attribute("b2")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("a1"),
                                                new Attribute("a2"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("a1"),
                                                new Attribute("a2")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("b1"),
                                                new Attribute("b2"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("b1"),
                                                new Attribute("b2"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-one relationship with a two-attribute foreign key")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case13() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "Contains",
                        new AttributeSet(),
                        new Key()
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Room", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("room_name"),
                                new Attribute("floor"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("room_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Building", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("latitude"),
                                new Attribute("longitude"),
                                new Attribute("building_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("latitude"),
                                new Attribute("longitude")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Building",
                            new AttributeSet(List.of(
                                new Attribute("latitude"),
                                new Attribute("longitude"),
                                new Attribute("building_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("latitude"),
                                new Attribute("longitude")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Room",
                            new AttributeSet(List.of(
                                new Attribute("room_name"),
                                new Attribute("floor"),
                                new Attribute("latitude"),
                                new Attribute("longitude"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("room_name")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("latitude"),
                                                new Attribute("longitude"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("latitude"),
                                                new Attribute("longitude"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A many-many relationship with a PK and one two-attribute FK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case14() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "PlaysFor",
                        new AttributeSet(List.of(
                                new Attribute("start_date"),
                                new Attribute("end_date"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("start_date")))) // primary key
        )));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Player", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("player_id"),
                                new Attribute("player_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("player_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Team", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("team_name"),
                                new Attribute("city"),
                                new Attribute("league"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("team_name"),
                                new Attribute("city")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Player",
                            new AttributeSet(List.of(
                                new Attribute("player_id"),
                                new Attribute("player_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("player_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Team",
                            new AttributeSet(List.of(
                                new Attribute("team_name"),
                                new Attribute("city"),
                                new Attribute("league"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("team_name"),
                                new Attribute("city")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "PlaysFor",
                            new AttributeSet(List.of(
                                new Attribute("player_id"),
                                new Attribute("city"),
                                new Attribute("team_name"),
                                new Attribute("start_date"),
                                new Attribute("end_date"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("player_id"),
                                new Attribute("city"),
                                new Attribute("team_name"),
                                new Attribute("start_date")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("player_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("player_id")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("city"),
                                                new Attribute("team_name"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("city"),
                                                new Attribute("team_name"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("ERD-to-DB worksheet, question 6: Two many-one relationships chained together")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case15() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "PaintedBy",
                        new AttributeSet(List.of(
                                new Attribute("date_painted"))),
                        new Key()),
                new Relationship(
                        "BornIn",
                        new AttributeSet(List.of(
                                new Attribute("birthdate"))),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Painting", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("painting_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("painting_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Artist", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("artist_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("artist_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE),
                                new Connection(input_erd.relationships.get(1), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()),
                new EntitySet(
                        "City", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("city_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("city_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(1), Multiplicity.ONE))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "City",
                            new AttributeSet(List.of(
                                new Attribute("city_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("city_name")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Artist",
                            new AttributeSet(List.of(
                                new Attribute("artist_name"),
                                new Attribute("city_name"),
                                new Attribute("birthdate"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("artist_name")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("city_name"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("city_name"))))))
        ));
        db.add( new Table( "Painting",
                            new AttributeSet(List.of(
                                new Attribute("painting_name"),
                                new Attribute("artist_name"),
                                new Attribute("date_painted"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("painting_name")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("Two many-many relationships 'chained' together")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case16() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "PaintedBy",
                        new AttributeSet(List.of(
                                new Attribute("date_painted"))),
                        new Key()),
                new Relationship(
                        "ResidentOf",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Painting", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("painting_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("painting_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Artist", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("artist_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("artist_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY),
                                new Connection(input_erd.relationships.get(1), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()),
                new EntitySet(
                        "City", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("city_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("city_name")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(1), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "City",
                            new AttributeSet(List.of(
                                new Attribute("city_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("city_name")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Artist",
                            new AttributeSet(List.of(
                                new Attribute("artist_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("artist_name")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Painting",
                            new AttributeSet(List.of(
                                new Attribute("painting_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("painting_name")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "PaintedBy",
                            new AttributeSet(List.of(
                                new Attribute("painting_name"),
                                new Attribute("artist_name"),
                                new Attribute("date_painted"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("painting_name"),
                                new Attribute("artist_name")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("painting_name"))),
                                        db.get(2),
                                        new AttributeSet(List.of(
                                                new Attribute("painting_name"))))))
        ));
        db.add( new Table( "ResidentOf",
                            new AttributeSet(List.of(
                                new Attribute("city_name"),
                                new Attribute("artist_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("city_name"),
                                new Attribute("artist_name")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("artist_name")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("city_name"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("city_name"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("Subclass hierarchy using PK with two attributes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case17() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "ManagerIsAnEmployee",
                        new AttributeSet(List.of(
                                new Attribute("dept"))),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Employee", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("employee_name"),
                                new Attribute("start_date"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("employee_name"),
                                new Attribute("start_date")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Manager", // name
                        new AttributeSet(),
                        new Key(), // primary key
                        new ConnectionList(), // connections
                        new ParentList(List.of( input_erd.relationships.get(0) )), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Employee",
                            new AttributeSet(List.of(
                                new Attribute("employee_name"),
                                new Attribute("start_date"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("employee_name"),
                                new Attribute("start_date")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Manager",
                            new AttributeSet(List.of(
                                new Attribute("employee_name"),
                                new Attribute("start_date"),
                                new Attribute("dept"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("employee_name"),
                                new Attribute("start_date")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("employee_name"),
                                                new Attribute("start_date"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("employee_name"),
                                                new Attribute("start_date"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A ternary relationship with multiplicity of many-one-one")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case18() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "Rel",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "R", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "S", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("b1"),
                                new Attribute("b2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("b2")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "T", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("c1"),
                                new Attribute("c2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("c1")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "T",
                            new AttributeSet(List.of(
                                new Attribute("c1"),
                                new Attribute("c2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("c1")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "S",
                            new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("b2")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "R",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"),
                                new Attribute("b2"),
                                new Attribute("c1"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("b2"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("b2")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("c1"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("c1"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("An entity set inherits from a subclass")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case19() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "LabIsACourse",
                        new AttributeSet(),
                        new Key()),
                new Relationship(
                        "VirtualIsALab",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Course", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("crn"),
                                new Attribute("section"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("crn")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Lab", // name
                        new AttributeSet(),
                        new Key(), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(1), Multiplicity.ONE))), // connections
                        new ParentList(List.of( input_erd.relationships.get(0) )), // parents
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Virtual", // name
                        new AttributeSet(),
                        new Key(), // primary key
                        new ConnectionList(), // connections
                        new ParentList(List.of( input_erd.relationships.get(1) )), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Course",
                            new AttributeSet(List.of(
                                new Attribute("crn"),
                                new Attribute("section"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("crn")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Lab",
                            new AttributeSet(List.of(
                                new Attribute("crn"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("crn")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("crn"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("crn"))))))
        ));
        db.add( new Table( "Virtual",
                            new AttributeSet(List.of(
                                new Attribute("crn"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("crn")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("crn"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("crn"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("An entity set inherits from a subclass with a two-attribute PK")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void case20() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "LabIsACourse",
                        new AttributeSet(),
                        new Key()),
                new Relationship(
                        "VirtualIsALab",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Course", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("course_code"),
                                new Attribute("semester"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Lab", // name
                        new AttributeSet(List.of(
                                new Attribute("TA"))),
                        new Key(), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(1), Multiplicity.ONE))), // connections
                        new ParentList(List.of( input_erd.relationships.get(0) )), // parents
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Virtual", // name
                        new AttributeSet(List.of(
                                new Attribute("zoom_link"))),
                        new Key(), // primary key
                        new ConnectionList(), // connections
                        new ParentList(List.of( input_erd.relationships.get(1) )), // parents
                        new SupportingRelationshipList())
        ));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Course",
                            new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Lab",
                            new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("TA"),
                                new Attribute("semester"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("course_code"),
                                                new Attribute("semester"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("course_code"),
                                                new Attribute("semester"))))))
        ));
        db.add( new Table( "Virtual",
                            new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester"),
                                new Attribute("zoom_link"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("course_code"),
                                new Attribute("semester")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("course_code"),
                                                new Attribute("semester"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("course_code"),
                                                new Attribute("semester"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("A weak entity set is supported by a weak entity set, at least one of which is in a non-supporting relationship")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void caseB1() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "FoundIn",
                        new AttributeSet(),
                        new Key()),
                new Relationship(
                        "PlacedIn",
                        new AttributeSet(),
                        new Key()),
                new Relationship(
                        "HeldIn",
                        new AttributeSet(),
                        new Key())
        ));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "Building", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("building_id"),
                                new Attribute("building_name"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("building_id")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "Room", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("room_number"),
                                new Attribute("max_capacity"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("room_number")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(1), Multiplicity.ONE),
                                new Connection(input_erd.relationships.get(2), Multiplicity.ONE))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList(List.of( input_erd.relationships.get(0) ))
                ),
                new EntitySet(
                        "Desk", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("desk_id"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("desk_id")))), // primary key
                        new ConnectionList(), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList(List.of( input_erd.relationships.get(1) ))
                ),
                new EntitySet(
                        "Class", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("crn"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("crn")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(2), Multiplicity.MANY))), // connections
                        new ParentList(), // parents
                        new SupportingRelationshipList()
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "Building",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("building_name"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("building_id")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Room",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number"),
                                new Attribute("max_capacity"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"))))))
        ));
        db.add( new Table( "Desk",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number"),
                                new Attribute("desk_id"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number"),
                                new Attribute("desk_id")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"),
                                                new Attribute("room_number"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"),
                                                new Attribute("room_number"))))))
        ));
        db.add( new Table( "Class",
                            new AttributeSet(List.of(
                                new Attribute("building_id"),
                                new Attribute("room_number"),
                                new Attribute("crn"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("crn")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"),
                                                new Attribute("room_number"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("building_id"),
                                                new Attribute("room_number"))))))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }

    @Test
    @DisplayName("ERD-to-DB worksheet question 3 (simplified). Multiplicity of many-many-one")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void caseB2() {
        ImplementMe myImplementation = new ImplementMe();

        // Input ERD object
        ERD input_erd = new ERD();

        input_erd.relationships = new RelationshipList(List.of(
                new Relationship(
                        "Rel",
                        new AttributeSet(),
                        new Key())));
        input_erd.entitySets = new EntitySetList(List.of(
                new EntitySet(
                        "R", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("a1"),
                                new Attribute("a2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("a1")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "S", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("b1"),
                                new Attribute("b2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("b2")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.MANY))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                ),
                new EntitySet(
                        "T", // name
                        new AttributeSet(List.of( // attributes
                                new Attribute("c1"),
                                new Attribute("c2"))),
                        new Key(new AttributeSet(List.of(
                                new Attribute("c1")))), // primary key
                        new ConnectionList(List.of(
                                new Connection(input_erd.relationships.get(0), Multiplicity.ONE))), // connections
                        new ParentList(), // parents = empty
                        new SupportingRelationshipList() 
                )));

        // Actual DB object
        Database db = new Database();
        db.add( new Table( "T",
                            new AttributeSet(List.of(
                                new Attribute("c1"),
                                new Attribute("c2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("c1")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "S",
                            new AttributeSet(List.of(
                                new Attribute("b1"),
                                new Attribute("b2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("b2")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "R",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("a2"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1")))),
                            new ForeignKeySet()
        ));
        db.add( new Table( "Rel",
                            new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("b2"),
                                new Attribute("c1"))),
                            new Key(new AttributeSet(List.of(
                                new Attribute("a1"),
                                new Attribute("b2")))),
                            new ForeignKeySet(List.of(
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("b2"))),
                                        db.get(1),
                                        new AttributeSet(List.of(
                                                new Attribute("b2")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("c1"))),
                                        db.get(0),
                                        new AttributeSet(List.of(
                                                new Attribute("c1")))),
                                new ForeignKey(
                                        new AttributeSet(List.of(
                                                new Attribute("a1"))),
                                        db.get(2),
                                        new AttributeSet(List.of(
                                                new Attribute("a1"))))
                                ))
        ));

        assertEquals(db, myImplementation.convertToDatabase(input_erd));
    }
}