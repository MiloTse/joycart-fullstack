type CartItemType = {
    productId: string;
    imgUrl: string;
    weight: string;
    title: string;
    price: number;
    count: number;
    selected?: boolean;
}

export type ListItemType = {
    shopId: string;
    shopName: string;
    selected?: boolean;
    cartList: Array<CartItemType>;
}
export type ResponseType = {
    code: number,
    message: string,
    data: Array<ListItemType>,
}

export type CartSubmitArray = Array<{
    productId: string;
    count: number;
}>
export type SubmitResponseType = {
    code: number;
    message: string;
    data: {
        orderId: string;
    }
}