package net.javaguides.springboot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.tradestorage.dao.TradeRepository;
import com.db.tradestorage.model.Trade;
import com.db.tradestorage.service.TradeService;

@RunWith(SpringRunner.class)
@SpringBootTest
class TradestorageApplicationTests {

	@Autowired
	private TradeService tradeService;
	
	@MockBean
	private TradeRepository tradeRepository;
	
	
	@Test
	public void testPersist() {
		Trade t = new Trade("t2", 3, "c3", "b3",  LocalDate.of(2024,8,8), LocalDate.of(2022,8,8), "F"); 
		when(tradeRepository.save(t)).thenReturn(t);
		assertEquals(1, tradeService.persist(t));
	}
	@Test
	public void testFindAll() {
		when(tradeRepository.findAll()).thenReturn(Stream.of(new Trade("t2", 3, "c3", "b3",  LocalDate.of(2024,8,8), LocalDate.of(2022,8,8), "F"),new Trade("t2", 3, "c3", "b3",  LocalDate.of(2028,9,9), LocalDate.of(2022,8,8), "F")).collect(Collectors.toList()));
		
		assertEquals(2, tradeService.findAll().size());
	}
	@Test
	void contextLoads() {
	}

}
