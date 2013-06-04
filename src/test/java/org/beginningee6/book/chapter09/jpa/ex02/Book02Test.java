package org.beginningee6.book.chapter09.jpa.ex02;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Book02エンティティのデータアクセスに関するテスト。
 */
@RunWith(Arquillian.class)
public class Book02Test {
	private static final Logger logger = Logger.getLogger(Book02Test.class.getName());
	
	@Deployment
	public static Archive<?> PersistDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Book02.class.getPackage())
			.addAsManifestResource("test-persistence.xml", "persistence.xml")
			.addAsManifestResource("jbossas-ds.xml")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		logger.info(archive.toString(true));

		return archive;
	}
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction userTransaction;
	
	@Before
	public void setUp() throws Exception {
		clearData();
	}
	
	private void clearData() throws Exception {
		userTransaction.begin();
		em.joinTransaction();

		logger.info("Dumping old records...");
		
		em.createQuery("DELETE FROM Book02").executeUpdate();
		userTransaction.commit();
	}
	
	/**
	 * Book02エンティティを永続化するテスト
	 */
	@Test
	public void testPersistABook() throws Exception {
		
		///// 準備 /////
		
		Book02 book = new Book02();
        book.setTitle("The Hitchhiker's Guide to the Galaxy");
        book.setPrice(12.5F);
        book.setDescription("Science fiction comedy book");
        book.setIsbn("1-84023-742-2");
        book.setNbOfPage(354);
        book.setIllustrations(false);

        ///// テスト /////
		
        userTransaction.begin();
        em.joinTransaction();
        
        em.persist(book);
        
        userTransaction.commit();

        ///// 検証 /////
		
        assertThat(book.getId(), is(notNullValue()));        
	}
	
	/**
	 * Book02エンティティが複数件永続化されている状況における
	 * 全件取得のテスト
	 */
	@Test
	public void testFindTwoBooks() throws Exception {
		
		///// 準備 /////
		
		Book02 book1 = new Book02(
				"Book 1 Title", 
				10.0F, 
				"Book 1 Description", 
				"1-11111-111-1", 
				111, 
				true);

		Book02 book2 = new Book02(
				"Book 2 Title", 
				20.0F, 
				"Book 2 Description", 
				"2-22222-222-2", 
				222, 
				true);

        userTransaction.begin();
        em.joinTransaction();
        
        em.persist(book1);
        em.persist(book2);
        
        userTransaction.commit();
        em.clear();

        ///// テスト /////
		
        List<Book02> books = em.createNamedQuery("Book02.findAllBooks", Book02.class).getResultList();

        ///// 検証 /////
		
        assertThat(books.size(), is(2));
        assertThat(books, hasItems(book1, book2));
	}

}
