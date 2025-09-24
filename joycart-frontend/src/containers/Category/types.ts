//返回内容类型 - 适配ResponseDTO格式
export type CategoryAndTagResponseType = {
    code: number;
    message: string;
    data:  {
        category: Array<{
            id: string;
            name: string;
        }>;
        tag: Array<string>;
    }
}
export type ProductType = {
    id:string;
    imgUrl: string;
    title:string;
    price:number;
    sales:number;
}

export type ProductResponseType = {
    code: number;
    message: string;
    data: Array<ProductType>;
}
export type CartType = {
    id:string;
    title:string;
    imgUrl:string;
    price: string;
    count:number;
}

export type CartResponseType = {
    code: number;
    message: string;
    data: CartType;
}