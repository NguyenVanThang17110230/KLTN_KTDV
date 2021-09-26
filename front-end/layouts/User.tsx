import React, { FC, ReactNode } from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import Header from "../containers/Header/Header";
import { Props } from "../package/Model";

export const UserLayout:FC = ({ children }: Props) => {
  return (
    <>
      <div className="relative md:ml-64 bg-blueGray-100">
        <Sidebar />
        <Header />
        <div className="px-4 md:px-10 mx-auto w-full -m-24">
          {children}
          {/* <FooterAdmin /> */}
        </div>
      </div>
    </>
  );
};