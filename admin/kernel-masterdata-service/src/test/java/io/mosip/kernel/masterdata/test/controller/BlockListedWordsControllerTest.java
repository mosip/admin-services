package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.BlocklistedWordListRequestDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockListedWordsControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;
	private RequestWrapper<BlocklistedWordListRequestDto> blocklistedwordsReq;
	private RequestWrapper<FilterValueDto> filValDto;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		blocklistedwordsReq = new RequestWrapper<BlocklistedWordListRequestDto>();
		BlocklistedWordListRequestDto blocklistedWordListRequestDto = new BlocklistedWordListRequestDto();
		List<String> blockListed = new ArrayList<String>();
		blockListed.add("damn");
		blocklistedWordListRequestDto.setBlocklistedwords(blockListed);
		blocklistedwordsReq.setRequest(blocklistedWordListRequestDto);

		FilterValueDto f = new FilterValueDto();
		FilterDto fdto = new FilterDto();
		fdto.setColumnName("word");
		fdto.setText("shit");
		fdto.setType("all");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(fdto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createBlockListedWordTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"damm\"\n" + "  }\n" + "}"))
				.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createBlockListedWordTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"damm\"\n" + "  }\n" + "}"))
				.andReturn(), "KER-MSD-071");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002updateBlockListedWordTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"oldWord\": \"damm\",\n"
								+ "  \"word\": \"damit\"\n" + "  }\n" + "}"))
						.andReturn(),
				null);
		;

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateBlockListedWordExceptWordTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/blocklistedwords/details").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word updated\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"damm\"\n" + "  }\n" + "}"))
				.andReturn(), "KER-MSD-008");
		
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateBlockListedWordExceptWordTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/blocklistedwords/details").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word updated\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"damit\"\n" + "  }\n" + "}"))
				.andReturn(), null);
		
	}
	@Test
	@WithUserDetails("global-admin")
	public void t020getAllBlockListedWordByLangCodeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/blocklistedwords/eng")).andReturn(),
				"KER-MSD-008");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005validWordTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/blocklistedwords/words")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(blocklistedwordsReq)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006getAllBlocklistedWordsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/blocklistedwords/all")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t007searchBlockListedWordsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords/words").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"blocklistedwords\": [\"damm\"   ]\n}}"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008blockListedWordsFilterValuesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/blocklistedwords/filtervalues")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"filters\":[\n { \"columnName\" : \"word \" ,\"text\":\"shit\",\"type\":\"unique\" }], \n"
						+ "   \"optionalFilters\": \"[]\",\n" + "    \"languageCode\": \"eng\" \n }}"))
				.andReturn(), "KER-MSD-999");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008blockListedWordsFilterValuesTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008blockListedWordsFilterValuesTest2() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(), "KER-MSD-322");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008blockListedWordsFilterValuesTest3() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009updateBlockListedWordStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/blocklistedwords").param("isActive", "true").param("word", "damit"))
				.andReturn(), null);
		;

	}

	@Test
	@WithUserDetails("global-admin")
	public void t011deleteBlockListedWordTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/blocklistedwords/damit")).andReturn(), null);
		
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010createBlockListedWordFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"\"\n" + "  }\n" + "}"))
						.andReturn(),
				"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t012updateBlockListedWordFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"oldWord\": \"abcd\",\n"
								+ "  \"word\": \"damit\"\n" + "  }\n" + "}"))
						.andReturn(),
				"KER-MSD-008");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013updateBlockListedWordExceptWordFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/blocklistedwords/details").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng1\",\n" + "    \"word\": \"damm\"\n" + "  }\n" + "}"))
				.andReturn(), "KER-MSD-999");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t013updateBlockListedWordFailTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": null,\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"oldWord\": \"abcd\",\n"
								+ "  \"word\": \"damit\"\n" + "  }\n" + "}"))
						.andReturn(),
				"KER-MSD-008");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t014getAllBlockListedWordByLangCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/blocklistedwords/eng1")).andReturn(),
				"KER-MSD-008");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015searchBlockListedWordsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords/words").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"blocklistedwords\": [\"dammmmm\"]\n}}"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t016blockListedWordsFilterValuesFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/blocklistedwords/filtervalues")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"filters\":[\n { \"columnName\" : \"word \" ,\"text\":\"damn\",\"type\":\"equals\" }], \n"
						+ "   \"optionalFilters\": \"[]\",\n" + "    \"languageCode\": \"eng\" \n }}"))
				.andReturn(), "KER-MSD-999");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017updateBlockListedWordStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/blocklistedwords").param("isActive", "true").param("word", "damn"))
				.andReturn(), "KER-MSD-008");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018deleteBlockListedWordFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/blocklistedwords/damit1")).andReturn(), "KER-MSD-008");

	}
	@Test
	@WithUserDetails("global-admin")
	public void t001createBlockListedWordTest3() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/blocklistedwords").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "   \"description\": \"Block listed word\",\n" + "    \"isActive\": true,\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"word\": \"damm%%\"\n" + "  }\n" + "}"))
				.andReturn(), "KER-MSD-999");
	}

}
