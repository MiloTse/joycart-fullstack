//返回内容类型 (已更新为ResponseDTO格式)
export type ResponseType = {
    code: number,
    message: string,
    data: {
        id: string;
        imgUrl: string;
        title: string;
        subtitle: string;
        price: number;
        sales: number;
        origin: string;
        specification: string;
        detail: string;
    }
}

//购物车返回内容类型 (已更新为ResponseDTO格式)
export type CartResponseType = {
    code: number,
    message: string,
    data: {
        count: number;
    }
}

//添加到购物车返回内容类型 (ResponseDTO格式)
export type AddToCartResponseType = {
    code: number,
    message: string,
    data: {
        productId: string;
        count: number;
        action: string;
        timestamp: string;
    }
}
