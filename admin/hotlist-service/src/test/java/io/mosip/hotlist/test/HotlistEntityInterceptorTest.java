package io.mosip.hotlist.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.entity.Hotlist;
import io.mosip.hotlist.entity.HotlistHistory;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.exception.HotlistAppUncheckedException;
import io.mosip.hotlist.interceptor.HotlistEntityInterceptor;
import io.mosip.hotlist.security.HotlistSecurityManager;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(SpringRunner.class)
@WebMvcTest
public class HotlistEntityInterceptorTest {

	@InjectMocks
	private HotlistEntityInterceptor interceptor;

	@Mock
	private HotlistSecurityManager securityManager;

	@Test
	public void testOnSaveHotlistEntity() throws HotlistAppException {
		when(securityManager.encrypt(Mockito.any())).thenReturn("id");
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		Object[] state = new Object[] { "id" };
		String[] propertyNames = new String[] { "idValue" };
		interceptor.onSave(entity, null, state, propertyNames, null);
	}

	@Test
	public void testOnSaveHotlistHistoryEntity() throws HotlistAppException {
		when(securityManager.encrypt(Mockito.any())).thenReturn("id");
		HotlistHistory entity = new HotlistHistory();
		entity.setIdValue("id");
		Object[] state = new Object[] { "id" };
		String[] propertyNames = new String[] { "idValue" };
		interceptor.onSave(entity, null, state, propertyNames, null);
	}

	@Test
	public void testOnSaveError() throws HotlistAppException {
		try {
			when(securityManager.encrypt(Mockito.any()))
					.thenThrow(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
			Hotlist entity = new Hotlist();
			entity.setIdValue("id");
			Object[] state = new Object[] { "id" };
			String[] propertyNames = new String[] { "idValue" };
			interceptor.onSave(entity, null, state, propertyNames, null);
		} catch (HotlistAppUncheckedException e) {
			assertTrue(
					e.getErrorCode().contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode()));
			assertTrue(e.getErrorText()
					.contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage()));
		}
	}

	@Test
	public void testOnLoadHotlistEntity() throws HotlistAppException {
		when(securityManager.decrypt(Mockito.any())).thenReturn("id");
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		Object[] state = new Object[] { "id" };
		String[] propertyNames = new String[] { "idValue" };
		interceptor.onLoad(entity, null, state, propertyNames, null);
	}

	@Test
	public void testOnLoadError() throws HotlistAppException {
		try {
			when(securityManager.decrypt(Mockito.any()))
					.thenThrow(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
			Hotlist entity = new Hotlist();
			entity.setIdValue("id");
			Object[] state = new Object[] { "id" };
			String[] propertyNames = new String[] { "idValue" };
			interceptor.onSave(entity, null, state, propertyNames, null);
		} catch (HotlistAppUncheckedException e) {
			assertTrue(
					e.getErrorCode().contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode()));
			assertTrue(e.getErrorText()
					.contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage()));
		}
	}

	@Test
	public void testOnFlushDirtyHotlistEntity() throws HotlistAppException {
		when(securityManager.encrypt(Mockito.any())).thenReturn("id");
		Hotlist entity = new Hotlist();
		entity.setIdValue("id");
		Object[] state = new Object[] { "id" };
		String[] propertyNames = new String[] { "idValue" };
		interceptor.onSave(entity, null, state, propertyNames, null);
	}

	@Test
	public void testOnFlushDirtyError() throws HotlistAppException {
		try {
			when(securityManager.encrypt(Mockito.any()))
					.thenThrow(new HotlistAppException(HotlistErrorConstants.AUTHENTICATION_FAILED));
			Hotlist entity = new Hotlist();
			entity.setIdValue("id");
			Object[] state = new Object[] { "id" };
			String[] propertyNames = new String[] { "idValue" };
			interceptor.onSave(entity, null, state, propertyNames, null);
		} catch (HotlistAppUncheckedException e) {
			assertTrue(
					e.getErrorCode().contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode()));
			assertTrue(e.getErrorText()
					.contentEquals(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage()));
		}
	}
}
