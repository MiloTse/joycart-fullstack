//返回内容类型
export type ResponseType = {
    message: string;
    data: Array<{
        id: string;
        keyword:string;

    }>
}