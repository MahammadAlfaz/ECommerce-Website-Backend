package com.project.ecommerce.service;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.ProductResponse;
import com.project.ecommerce.exception.APINotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final FileService fileService;
    @Value("${product.path}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()->
                        new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        boolean existingProduct=productRepository.existsByProductNameIgnoreCaseAndCategory_CategoryId(productDTO.getProductName(),categoryId);
        if(existingProduct){
            throw new APINotFoundException("Product Already Exists!!!");
        }
        double specialPrice= productDTO.getPrice()-(productDTO.getDiscount()/100.0* productDTO.getPrice());
        Product product=modelMapper.map(productDTO,Product.class);
        product.setCategory(category);
        product.setImage("Default.png");
        product.setSpecialPrice(specialPrice);
        Product SavedProduct=productRepository.save(product);
        return modelMapper.map(SavedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> product=productRepository.findAll(pageable);
        List<Product> products=product.getContent();
         if(products.isEmpty()){
             throw  new APINotFoundException("No products found");
         }
         List<ProductDTO> response=products.stream()
                 .map(e->modelMapper.map(e, ProductDTO.class)).toList();
         ProductResponse productResponse=new ProductResponse();
         productResponse.setContent(response);
         productResponse.setPageNumber(product.getNumber());
         productResponse.setPageSize(product.getSize());
         productResponse.setTotalPages(product.getTotalPages());
        productResponse.setTotalItems(product.getTotalElements());
         productResponse.setHasNext(product.hasNext());
         return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        Page<Product> product=productRepository.findByCategory(category,pageable);
        List<ProductDTO> productDTOS =product.getContent().stream().map(
                e->modelMapper.map(e, ProductDTO.class))
                        .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(product.getNumber());
        productResponse.setPageSize(product.getSize());
        productResponse.setTotalPages(product.getTotalPages());
        productResponse.setTotalItems(product.getTotalElements());
        productResponse.setHasNext(product.hasNext());
        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> product=productRepository.findByProductNameContainsIgnoreCase(keyword,pageable);
        List<ProductDTO> productDTOS =product.getContent().stream().map(
                        e->modelMapper.map(e, ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(product.getNumber());
        productResponse.setPageSize(product.getSize());
        productResponse.setTotalPages(product.getTotalPages());
        productResponse.setTotalItems(product.getTotalElements());
        productResponse.setHasNext(product.hasNext());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","productId",productId)
        );
        double specialPrice= productDTO.getPrice()-(productDTO.getDiscount()/100.0* productDTO.getPrice());
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setSpecialPrice(specialPrice);
        return modelMapper.map(existingProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(
                ()->  new ResourceNotFoundException("Product","product",productId)
        );
        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","productId",productId)
        );
        String fileName=fileService.uploadImage(path,image);
        product.setImage(fileName);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);


    }


}
