
//购物车数量变更返回内容类型 (已更新为ResponseDTO格式)
export type CartChangeResponseType = {
    code: number,
    message: string,
    data: {
        id: number;
        count: number;
        action: string;
        updatedAt: string;
    }
}
