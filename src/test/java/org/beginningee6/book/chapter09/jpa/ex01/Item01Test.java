package org.beginningee6.book.chapter09.jpa.ex01;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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

@RunWith(Arquillian.class)
public class Item01Test {
	private static final Logger logger = Logger.getLogger(Item01Test.class.getName());
	
	@Deployment
	public static Archive<?> PersistDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Item01.class.getPackage())
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
		
		em.createQuery("DELETE FROM Item01").executeUpdate();
		userTransaction.commit();
	}
	
	/**
	 * Item01エンティティを永続化するテスト。
	 */
	@Test
	public void testPersistAnItem() throws Exception {
		
		///// 準備 /////
		
		Item01 item = new Item01();
		item.setTitle("The Hitchhiker's Guide to the Galaxy");
		item.setPrice(12.5F);
		item.setDescription("Science fiction comedy book");
		
        ///// テスト /////
		
        userTransaction.begin();
        em.joinTransaction();
        
        em.persist(item);
        
        userTransaction.commit();
        
        ////// 検証 /////
        
        assertThat(item.getId(), is(notNullValue()));        
        assertThat(item.getTitle(), 			is("The Hitchhiker's Guide to the Galaxy"));
        assertThat(item.getPrice(), 			is(12.5F));
        assertThat(item.getDescription(), 		is("Science fiction comedy book"));
        // Itemエンティティが生成された時の在庫数量の初期値は0
        assertThat(item.getAvailableInStock(), 	is(0));
	}
	
	/**
	 * 永続化されたItem01エンティティを全件取得するテスト。
	 */
	@Test
	public void testFindItems() throws Exception {
		
		///// 準備 /////
		
		Item01 item = new Item01();
		item.setTitle("The Hitchhiker's Guide to the Galaxy");
		item.setPrice(12.5F);
		item.setDescription("Science fiction comedy book");
		
        userTransaction.begin();
        em.joinTransaction();
        em.persist(item);
        userTransaction.commit();
		
        ///// テスト /////
		
        List<Item01> items = 
        	em.createNamedQuery("Item01.findAllItems", Item01.class)
        		.getResultList();
        
        ////// 検証 /////
        
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getTitle(), 			is("The Hitchhiker's Guide to the Galaxy"));
        assertThat(items.get(0).getPrice(), 			is(12.5F));
        assertThat(items.get(0).getDescription(), 		is("Science fiction comedy book"));
        assertThat(items.get(0).getAvailableInStock(), 	is(0));
	}

	/**
	 * 在庫数量が0のItem01エンティティが永続化されている状況で、
	 * 在庫数量をひとつ増やすテスト。
	 */
	@Test
	public void testIncreaseAvailability() throws Exception {
		
		///// 準備 /////
		
		Item01 item = new Item01();
		item.setTitle("The Hitchhiker's Guide to the Galaxy");
		item.setPrice(12.5F);
		item.setDescription("Science fiction comedy book");

        userTransaction.begin();
        em.joinTransaction();
        // 在庫数量＝0でItem01エンティティが永続化される。
        em.persist(item);
        userTransaction.commit();
		
        ///// テスト /////
		
        userTransaction.begin();
        em.joinTransaction();
        
        Item01 persisted = em.find(Item01.class, item.getId());
        // 在庫数量をひとつ増やす
        persisted.increaseAvailableStock();
        
        userTransaction.commit();
        
        ////// 検証 /////
        
        // 在庫数量が0から1に変更されたことを確認する
        Item01 updated = em.find(Item01.class, item.getId());
        assertThat(updated.getAvailableInStock(), 	is(1));
	}

	/**
	 * 在庫数量が1の商品に対して、数量をひとつ減らすテスト。
	 */
	@Test
	public void testDecreaseAvailabilityWhenStockHasOneItem() throws Exception {
		
		///// 準備 /////
		
		Item01 item = new Item01();
		item.setTitle("The Hitchhiker's Guide to the Galaxy");
		item.setPrice(12.5F);
		item.setDescription("Science fiction comedy book");

        userTransaction.begin();
        em.joinTransaction();
        // Item01エンティティを永続化（在庫数量＝0）
        em.persist(item);
        // 在庫数量をひとつ増やす（在庫数量＝1）
        item.increaseAvailableStock();
        userTransaction.commit();
		
        ///// テスト /////
		
        userTransaction.begin();
        em.joinTransaction();
        
        Item01 persisted = em.find(Item01.class, item.getId());
        // 在庫数量をひとつ減らす
        persisted.decreaseAvailableStock();
        
        userTransaction.commit();
        
        ////// 検証 /////
        
        // 在庫数量が1から0に変更されたことを確認する
        Item01 updated = em.find(Item01.class, item.getId());
        assertThat(updated.getAvailableInStock(), 	is(0));
	}

	/**
	 * 在庫数量が0の商品に対して、数量をひとつ減らすテスト。
	 * この場合、例外：StockAvailabilityExceptionがスローされる。
	 */
	@Test
	public void testDecreaseAvailabilityWhenStockHasNoItem() throws Exception {
		
		///// 準備 /////
		
		Item01 item = new Item01();
		item.setTitle("The Hitchhiker's Guide to the Galaxy");
		item.setPrice(12.5F);
		item.setDescription("Science fiction comedy book");

        userTransaction.begin();
        em.joinTransaction();
        // Item01エンティティを永続化（在庫数量＝0）
        em.persist(item);
        userTransaction.commit();

        ///// テストと検証 /////
        
        userTransaction.begin();
        em.joinTransaction();

        try {
			Item01 persisted = em.find(Item01.class, item.getId());
			// 在庫数量をひとつ減らす
			// ⇒すでに数量は0なので例外がスローされる
            persisted.decreaseAvailableStock();

            fail("Should throw Exception");
        } catch (Exception e) {
        	// スローされた例外がStockAvailabilityExceptionであることを確認
        	assertThat(e, is(instanceOf(StockAvailabilityException.class)));
        	userTransaction.rollback();
        }

	}

}
