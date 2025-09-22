//返回内容类型
export type ResponseType = {
    success: boolean,
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
