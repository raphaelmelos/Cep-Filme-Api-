package br.org.fundatec.cep.controller;

import br.org.fundatec.cep.CepApiApplication;
import br.org.fundatec.cep.exception.handler.ErroResponse;
import br.org.fundatec.cep.model.Cep;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = CepApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CepControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    private MockMvc mockMvc;

    private static final Integer CEP_INSERIR = 95680807;

    private Cep build(Integer cep, String uf, String cidade, String logradouro) {
        return new Cep(cep, cidade, uf, logradouro);
    }

    @Test
    void testaAdicaoCep()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(CEP_INSERIR, "RS", "Canela", "Teste 1"))))
                .andExpect(status().is(HttpStatus.CREATED.value())).andReturn();

        Cep retorno = parseResponse(mvcResult, Cep.class);
        assertThat("Não retornou o CEP Correto", retorno.getCep(), equalTo(CEP_INSERIR));
    }


    @Test
    void testaAdicaoCepInvalido()  throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(15680807, "RS", "Canela", "Teste 1"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a calidação correta", retorno.getMensagem(), containsString("Cep nulo ou em faixa invalida"));
    }

    @Test
    void testaAdicaoUFNulo()  throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(95680807, null, "Canela", "Teste 1"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a calidação correta", retorno.getMensagem(), containsString("uf - UF nao pode ser nulo"));
    }


    @Test
    void testaAdicaoUFTamanhoInvalido()  throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(95680807, "RSS", "Canela", "Teste 1"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a calidação correta", retorno.getMensagem(), containsString("UF nao pode ter mais de dois caracteres"));
    }


    @Test
    void testaAdicaoCidadeNula()  throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(95680807, "RS", null, "Teste 1"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a calidação correta", retorno.getMensagem(), containsString("Cidade nao pode ser nulo"));
    }

    @Test
    void testaLista()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps")).andExpect(status().is(HttpStatus.OK.value())).andReturn();

        List<Cep> ceps = parseResponseList(mvcResult, Cep.class);
        assertThat("Não retornou quantidade correta", ceps.size(), is(1));

        Cep validar = build(95680808, null, null, null);
        assertThat("Não retornou cep correto", ceps.get(0), equalTo(validar));
    }

    @Test
    void testaBuscaPorCep()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps/95680808")).andExpect(status().is(HttpStatus.OK.value())).andReturn();

        Cep cepRetorno = parseResponse(mvcResult, Cep.class);

        Cep validar = build(95680808, null, null, null);
        assertThat("Não retornou cep correto", cepRetorno, equalTo(validar));
    }


    @Test
    void testaBuscaPorCepNaoEcontrado()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps/95680800")).andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a mensagem correta", retorno.getMensagem(), equalTo("Cep: 95680800 nao encontrado"));
    }


    @Test
    void testaBuscaPorCidade()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps/consulta?cidade=Cane")).andExpect(status().is(HttpStatus.OK.value())).andReturn();

        List<Cep> cepsRetorno = parseResponseList(mvcResult, Cep.class);

        Cep validar = build(95680808, null, null, null);
        assertThat("Não retornou a quantidade correta", cepsRetorno.size(), equalTo(1));
        assertThat("Não retornou cep correto", cepsRetorno.get(0), equalTo(validar));
    }


    @Test
    void testaBuscaPorCidadeNaoEcontrada()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps/consulta?cidade=Porto")).andExpect(status().is(HttpStatus.OK.value())).andReturn();

        List<Cep> cepsRetorno = parseResponseList(mvcResult, Cep.class);

        Cep validar = build(95680808, null, null, null);
        assertThat("Não retornou a quantidade correta", cepsRetorno.size(), equalTo(0));
    }


    @Test
    void testaEdicaoCep()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(put("/ceps/95680808").contentType("application/json")
                                .content(MAPPER.writeValueAsString(build(CEP_INSERIR, "RS", "Canela", "Teste 3"))))
                        .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        Cep retorno = parseResponse(mvcResult, Cep.class);
        assertThat("Não retornou o CEP Correto", retorno.getCep(), equalTo(95680808));
        assertThat("Não fez a aleteracao", retorno.getLogradouro(), equalTo("Teste 3"));
    }


    @Test
    void testaEdicaoCepEncontrado()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(put("/ceps/95680800").contentType("application/json")
                                .content(MAPPER.writeValueAsString(build(CEP_INSERIR, "RS", "Canela", "Teste 3"))))
                        .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a mensagem correta", retorno.getMensagem(), equalTo("Cep: 95680800 nao encontrado"));
    }


    @Test
    void testaRemocaoCep()  throws Exception{
        mockMvc.perform(post("/ceps").contentType("application/json")
                        .content(MAPPER.writeValueAsString(build(CEP_INSERIR, "RS", "Canela", "Teste 1"))))
                .andExpect(status().is(HttpStatus.CREATED.value())).andReturn();

        MvcResult mvcResult =
                mockMvc.perform(delete("/ceps/"+CEP_INSERIR).contentType("application/json"))
                        .andExpect(status().is(HttpStatus.OK.value())).andReturn();
    }

    @Test
    void testaRemocaoCepEncontrado()  throws Exception{
        MvcResult mvcResult =
                mockMvc.perform(delete("/ceps/95680800").contentType("application/json"))
                        .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();

        ErroResponse retorno = parseResponse(mvcResult, ErroResponse.class);
        assertThat("Não retornou a mensagem correta", retorno.getMensagem(), equalTo("Cep: 95680800 nao encontrado"));
    }



    @AfterEach
    public void tearDown()  throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(get("/ceps/"+ CEP_INSERIR)).andReturn();

        if(mvcResult.getResponse().getStatus() != HttpStatus.NOT_FOUND.value()) {
            mockMvc.perform(delete("/ceps/"+CEP_INSERIR)).andReturn();
        }
    }


    private static <T> List<T> parseResponseList(MvcResult mockHttpServletResponse, Class<T> clazz) {
        try {
            String contentAsString = mockHttpServletResponse.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T parseResponse(MvcResult mockHttpServletResponse, Class<T> clazz) {
        try {
            String contentAsString = mockHttpServletResponse.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}