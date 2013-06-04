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
 * CD02エンティティのデータアクセスに関するテスト。
 */
@RunWith(Arquillian.class)
public class CD02Test {
	private static final Logger logger = Logger.getLogger(CD02Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(CD02.class.getPackage())
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
		
		em.createQuery("DELETE FROM CD02").executeUpdate();
		userTransaction.commit();
	}
	
	/**
	 * CD02エンティティを永続化するテスト
	 */
	@Test
	public void testPersistACD() throws Exception {
		
		///// 準備 /////
		
        CD02 cd = new CD02(
        		"Title 1",
        		10.0F,
        		"Title 1 Description",
        		null,
        		"Music Company 1",
        		1,
        		100.0F,
        		"male");

        ///// テスト /////
		
        userTransaction.begin();
        em.joinTransaction();
        
        em.persist(cd);
        
        userTransaction.commit();

        ///// 検証 /////
		
        assertThat(cd.getId(), is(notNullValue()));        
	}
	
	/**
	 * CD02エンティティが複数件永続化されている状況における
	 * 全件取得のテスト
	 */
	@Test
	public void testFindTwoCDs() throws Exception {
		
		///// 準備 /////
		
        CD02 cd1 = new CD02(
        		"Title 1",
        		10.0F,
        		"Title 1 Description",
        		null,
        		"Music Company 1",
        		1,
        		100.0F,
        		"male");

        CD02 cd2 = new CD02(
        		"Title 2",
        		20.0F,
        		"Title 2 Description",
        		null,
        		"Music Company 2",
        		2,
        		200.0F,
        		"female");

        userTransaction.begin();
        em.joinTransaction();
        
        em.persist(cd1);
        em.persist(cd2);
        
        userTransaction.commit();
        em.clear();

        ///// テスト /////
		
        List<CD02> books = em.createNamedQuery("CD02.findAllCDs", CD02.class).getResultList();

        ///// 検証 /////
		
        assertThat(books.size(), is(2));
        assertThat(books, hasItems(cd1, cd2));
	}

}
