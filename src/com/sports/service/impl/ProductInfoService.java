package com.sports.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sports.dto.ProdOptionDTO;
import com.sports.dto.ProductFileDTO;
import com.sports.dto.ProductInfoDTO;
import com.sports.dto.ProductInfoOptionDTO;
import com.sports.persistance.mapper.ProductInfoMapper;
import com.sports.service.IProductInfoService;
import com.sports.util.CmmUtil;

@Service("ProductInfoService")
public class ProductInfoService implements IProductInfoService{
	@Resource(name="ProductInfoMapper")
	private ProductInfoMapper productInfoMapper;

	@Override
	public List<ProductInfoDTO> getCategoryParents() throws Exception {
		return productInfoMapper.getCategoryParents();
	}

	@Override
	public List<ProductInfoDTO> getCategoryChild(ProductInfoDTO pDTO) throws Exception {
		return productInfoMapper.getCategoryChild(pDTO);
	}

	@Override
	public int insertProduct(ProductInfoDTO pDTO, ProductFileDTO fDTO) throws Exception {
		productInfoMapper.insertProduct(pDTO);
		fDTO.setProd_no(pDTO.getProd_no());
		return productInfoMapper.insertProductMainFile(fDTO);
	}

	@Override
	public List<ProductInfoDTO> getProductList() throws Exception {
		return productInfoMapper.getProductList();
	}

	@Override
	public List<ProductInfoDTO> getProductSelectList(ProductInfoDTO pDTO) throws Exception {
		return productInfoMapper.getProductSelectList(pDTO);
	}
	
	/**
	 * 제품 상세 데이터를 가져올 서비스 이다. 옵션이 여러개일수 있기때문에 맵퍼를 두번 호출하여 Map으로 두개의 객체를 return한다.
	 */
	@Override
	public Map<String, Object> getProductDetail(ProductInfoDTO pDTO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//제품의 상세를 가져온다.
		ProductInfoDTO prodDTO = productInfoMapper.getProductDetail(pDTO);
		if(prodDTO == null){
			prodDTO = new ProductInfoDTO();
		}
		//제품의 옵션을 가져온다. 옵션은 Map형태로 정제한다
		String prodNo = CmmUtil.nvl(pDTO.getProd_no());
		List<ProdOptionDTO> pList = productInfoMapper.getProductOption(prodNo);
		if(pList == null){
			pList = new ArrayList<ProdOptionDTO>();
		}
		System.out.println("pList.size() : " + pList.size());
		
		Map<String, List<ProdOptionDTO>> pMap = new HashMap<String, List<ProdOptionDTO>>();
		if(pList.size() != 0){
			for(ProdOptionDTO optDTO : pList){
				if(pMap.containsKey(optDTO.getCode_name())){
					List<ProdOptionDTO> tmp = pMap.get(optDTO.getCode_name());
					tmp.add(optDTO);
					pMap.put(optDTO.getCode_name(), tmp);
				}else{
					List<ProdOptionDTO> newProdOptList = new ArrayList<ProdOptionDTO>();
					newProdOptList.add(optDTO);
					pMap.put(optDTO.getCode_name(), newProdOptList);
				}
			}
		}
		System.out.println("pMap.size : " + pMap.size());
		//return할 객체에 담는다.
		resultMap.put("prodDetail", prodDTO);
		resultMap.put("prodOpt", pMap);
		
		//return 시킨다.
		return resultMap;
	}

	@Override
	public int insertProduct(ProductInfoDTO pDTO, ProductFileDTO fDTO, ProductFileDTO fdDTO) throws Exception {
		productInfoMapper.insertProduct(pDTO);
		fDTO.setProd_no(pDTO.getProd_no());
		fdDTO.setProd_no(pDTO.getProd_no());
		productInfoMapper.insertProductMainFile(fDTO);
		return productInfoMapper.insertProductDetailFile(fdDTO);
	}

	@Override
	public List<ProductInfoDTO> getSelectOption() throws Exception {
		return productInfoMapper.getSelectOption();
	}

	@Override
	public int insertProduct(ProductInfoDTO pDTO, ProductFileDTO fDTO, ProductFileDTO fdDTO, List<ProductInfoDTO> optList) throws Exception {
		productInfoMapper.insertProduct(pDTO);
		fDTO.setProd_no(pDTO.getProd_no());
		fdDTO.setProd_no(pDTO.getProd_no());
		List<ProductInfoDTO> setList = new ArrayList<ProductInfoDTO>();
		for(ProductInfoDTO optDTO : optList){
			ProductInfoDTO setDTO = new ProductInfoDTO();
			setDTO.setOpt_kind(optDTO.getOpt_kind());
			setDTO.setOpt_name(optDTO.getOpt_name());
			setDTO.setOpt_price(optDTO.getOpt_price());
			setDTO.setProd_no(pDTO.getProd_no());
			setList.add(setDTO);
		}
		productInfoMapper.insertProductMainFile(fDTO);
		productInfoMapper.insertProductOption(setList);
		return productInfoMapper.insertProductDetailFile(fdDTO);
	}

	@Override
	public int insertProduct(ProductInfoDTO pDTO, ProductFileDTO fDTO, List<ProductInfoDTO> optList) throws Exception {
		productInfoMapper.insertProduct(pDTO);
		fDTO.setProd_no(pDTO.getProd_no());
		List<ProductInfoDTO> setList = new ArrayList<ProductInfoDTO>();
		for(ProductInfoDTO optDTO : optList){
			ProductInfoDTO setDTO = new ProductInfoDTO();
			setDTO.setOpt_kind(optDTO.getOpt_kind());
			setDTO.setOpt_name(optDTO.getOpt_name());
			setDTO.setOpt_price(optDTO.getOpt_price());
			setDTO.setProd_no(pDTO.getProd_no());
			setList.add(setDTO);
		}
		productInfoMapper.insertProductOption(setList);
		return productInfoMapper.insertProductMainFile(fDTO);
	}
}
