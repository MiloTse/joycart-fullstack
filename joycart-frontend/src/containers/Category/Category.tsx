import './style.scss';
import React, {useEffect, useState} from "react";

import useRequest from "../../utils/useRequest";
import {CategoryAndTagResponseType, ProductResponseType, ProductType, CartType, CartResponseType} from "./types";
import {message} from "../../utils/message";
import NavBar from "../../components/NavBar/NavBar";
import {useNavigate} from "react-router-dom";
import Popover from "../../components/Popover/Popover";
import {CartChangeResponseType} from "../../types";


const Category = () => {
    const navigate = useNavigate();
    //store data of item list
    const [products, setProducts] = useState<Array<ProductType>>([]);
    const [categories, setCategories] = useState<Array<{ id: string; name: string }>>([]);
    //handle data of re-sending request
    const [tags, setTags] = useState<string[]>([]);
    const [keyword, setKeyword] = useState('');
    //logic of shopping cart
    const [showCart, setShowCart] = useState<boolean>(false);
    const [cartProductInfo, setCartProductInfo] = useState<CartType>({
        id:'', title:'', imgUrl:'', price: '', count:0,
    });


    const [currentTag, setCurrentTag] = useState('');
    const  [ currentCategory,  setCurrentCategory ]=useState('');
    //sending request dynamically based on page data changed
    const {request: tagRequest  } = useRequest<CategoryAndTagResponseType>({manual: true});
    //passing request data productRequestData to useRequest
    const {request: productRequest  } = useRequest<ProductResponseType>({manual: true});
    //gain cart's info
    const {request: cartRequest  } = useRequest<CartResponseType>({manual: true});
    //update cart's content
    const {request: cartChangeRequest  } = useRequest<CartChangeResponseType>({manual: true});

     useEffect(() => {
         productRequest({
                url:'/categoryProduct.json',
                method:'GET',
                // The original POST request code was implemented using Charles Proxy, and it is currently commented out to facilitate future conversion into a full-stack project.
                // method:'POST',
                // data: {
                //     tag:currentTag,
                //     keyword ,
                //     category: currentCategory,
                // },
            }
        ).then((data)=>{
            if(data?.success) {
                const result = data.data;
                setProducts(result);
            }
        }).catch((e:any)=>{
            message(e?.message);
        });
    },[keyword, currentTag, currentCategory,productRequest]);



    useEffect(() => {
        tagRequest({
                url:'/categoryAndTagList.json',
                method:'GET',

            }
        ).then((data)=>{
             if(data?.success) {
                 const result = data.data;
                console.log(result);
                setCategories(result.category);
                setTags(result.tag);//should same as ts file and json
            }
        }).catch((e:any)=>{
            message(e?.message);
        });
    },[tagRequest]);

    //handle search content change
    function handleKeyDown(key: string, target: EventTarget & HTMLInputElement) {
        if(key === 'Enter') {
            //HTMLInputElement must have value
            // console.log(target.value);
            setKeyword(target.value);
        }
    }
    console.log(products)

    function handleProductClick(e: React.MouseEvent<HTMLDivElement>, productId:string ) {
        e.stopPropagation();
        cartRequest ({
                url:'/cartProductInfo.json',
                method:'GET',
                params: {
                    productId,
                },
            }
        ).then((data)=>{
            //if success, load data from response api and set to cartProductInfo and show popover
            setCartProductInfo(data.data);
            setShowCart(true);
        }).catch((e:any)=>{
            message(e?.message);
        });

    }

    function closeMask() {
        setShowCart(false);
    }

    function handleCartNumberChange(number: number) {
        const newCartProductInfo = {...cartProductInfo};
        const {count} = newCartProductInfo;
        newCartProductInfo.count = count + number;
        if(newCartProductInfo.count < 0) {
            newCartProductInfo.count = 0;
        }
        setCartProductInfo(newCartProductInfo);
    }

    function changeCartInfo() {
        cartChangeRequest({
            url: '/cartChange.json',
            method: 'GET',
            params: {id: cartProductInfo.id, count: cartProductInfo.count},
        }).then(response => {
            if(response.success){
                 setShowCart(false);
            }
        }).catch((e)=>{
            message(e.message);
        })
    }

    return (
        <div className="page category-page">
            <div className="title">

                <div className="text">Category</div>
            </div>
            <div className="search">
                <div className="search-area">
                    <div className="search-icon iconfont">&#xe600;</div>
                    <input
                        className="search-input"
                        placeholder="Please enter product name"
                        onKeyDown={(e) => {handleKeyDown(e.key, e.currentTarget)} }
                    />
                </div>

            </div>
            <div className="category">
                <div className={currentCategory===''? 'category-item category-item-active':'category-item'}
                onClick={()=>{setCurrentCategory('')}}
                >全部商品</div>
                {
                    categories.map((category)=>{
                        return(
                            <div key={category.id}
                                 className={category.id === currentCategory ? "category-item category-item-active":"category-item "}
                                 onClick={()=>{setCurrentCategory(category.id)}}
                            >
                                {category.name}
                            </div>)
                    })
                }
            </div>
            <div className="tag">
                <div className= { currentTag ==='' ? 'tag-item tag-item-active':'tag-item'}
                     onClick={()=>{setCurrentTag('')}}
                >
                    全部
                </div>
                {
                    tags.map((tag,index)=>(
                            <div className= {tag===currentTag? 'tag-item tag-item-active':'tag-item'}
                                 key={tag+index}
                                 onClick={()=>{setCurrentTag(tag)}}
                            >
                                {tag}
                            </div>
                    ))
                }


            </div>
            <div className="product">
                <div className="product-title">精选商品({products.length})</div>
                {
                    products.map((product)=> {

                            return(
                                <div className="product-item"
                                     onClick={()=>{
                                         navigate(`/detail/${product.id}`);
                                     }}
                                     key={product.id}
                                >
                                    <img className="product-item-img"
                                         src={product.imgUrl}
                                         alt={product.title}
                                    />
                                    <div className="product-item-content">
                                        <div className="product-item-title">{product.title}</div>
                                        <div className="product-item-sales">sold {product.sales}</div>
                                        <div className="product-item-price">
                                            <span className="product-item-price-symbol">&#36;  </span>
                                            {product.price}
                                        </div>
                                        <div className="product-item-button"
                                             onClick={(e)=>{handleProductClick(e, product.id)}}
                                        >
                                            buy
                                        </div>
                                    </div>
                                </div>
                            )

                    }

                    )
                }


                <div className="product-item">
                    <img className="product-item-img"
                        src="/images/external/fresh-1.png"
                        alt=""
                    />
                    <div className="product-item-content">
                        <div className="product-item-title">chicken wing middle 1000g/...</div>
                        <div className="product-item-sales">sold 156</div>
                        <div className="product-item-price">
                            <span className="product-item-price-symbol">&#36;  </span>156
                        </div>
                        <div className="product-item-button"

                        >
                            buy
                        </div>
                    </div>
                </div>

            </div>
            <NavBar activeName='category'/>
            <Popover show={showCart} blankClickCallBack={ closeMask}>
                <div className='cart'>
                    <div className='cart-content'>
                        <img className='cart-content-img' alt={cartProductInfo.title} src={cartProductInfo.imgUrl}/>
                        <div className='cart-content-info'>
                            <div className='cart-content-title'> {cartProductInfo.title} </div>
                            <div className='cart-content-price'>
                                <span className='cart-content-price-symbol'>&#36;</span>
                                {cartProductInfo.price}
                            </div>
                        </div>
                    </div>
                    <div className='cart-count'>
                        <div className='cart-count-content'>
                            Quantity:
                            <div className='cart-count-counter'>
                                <div className='cart-count-button'
                                     onClick={()=>{handleCartNumberChange(-1)}}
                                >
                                    -
                                </div>
                                <div className='cart-count-text'>
                                    {cartProductInfo.count}
                                </div>
                                <div className='cart-count-button'
                                     onClick={()=>{handleCartNumberChange(+1)}}
                                >
                                    +
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className='cart-buttons'>
                        <div className='cart-button cart-button-left' onClick={changeCartInfo}>Add to Cart</div>
                        <div className='cart-button cart-button-right'>Buy Now</div>
                    </div>
                </div>
            </Popover>
        </div>
    )
}

export default Category;